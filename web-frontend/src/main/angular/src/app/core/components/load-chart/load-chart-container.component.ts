import {
    Component,
    OnInit,
    OnDestroy,
    Input,
    ComponentFactoryResolver,
    Injector,
    ChangeDetectionStrategy,
    ChangeDetectorRef
} from '@angular/core';
import {Subject, forkJoin, merge, of, EMPTY} from 'rxjs';
import {filter, map, tap, switchMap, catchError, pluck, withLatestFrom, takeUntil} from 'rxjs/operators';
import {TranslateService} from '@ngx-translate/core';
import {PrimitiveArray, Data, areaStep} from 'billboard.js';
import * as moment from 'moment-timezone';

import {
    WebAppSettingDataService,
    AnalyticsService,
    TRACKED_EVENT_LIST,
    DynamicPopupService,
    MESSAGE_TO,
    StoreHelperService,
    MessageQueueService,
    AgentHistogramDataService,
    NewUrlStateNotificationService
} from 'app/shared/services';
import {
    HELP_VIEWER_LIST,
    HelpViewerPopupContainerComponent
} from 'app/core/components/help-viewer-popup/help-viewer-popup-container.component';
import {ServerMapData} from 'app/core/components/server-map/class/server-map-data.class';
import {getMaxTickValue, getStackedData} from 'app/core/utils/chart-util';
import {Actions} from 'app/shared/store/reducers';
import {
    ServerErrorPopupContainerComponent
} from 'app/core/components/server-error-popup/server-error-popup-container.component';

export enum SourceType {
    MAIN = 'MAIN',
    FILTERED = 'FILTERED',
    INFO_PER_SERVER = 'INFO_PER_SERVER'
}

export enum Layer {
    LOADING = 'loading',
    RETRY = 'retry',
    CHART = 'chart'
}

@Component({
    selector: 'pp-load-chart-container',
    templateUrl: './load-chart-container.component.html',
    styleUrls: ['./load-chart-container.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoadChartContainerComponent implements OnInit, OnDestroy {
    @Input() sourceType: string;

    private unsubscribe = new Subject<void>();
    private serverMapData: ServerMapData;
    private isOriginalNode: boolean;
    private selectedAgent = '';
    private chartColors: string[];
    private defaultYMax = 10;
    private timezone: string;
    private dateFormatMonth: string;
    private dateFormatDay: string;

    private originalTarget: ISelectedTarget; // from serverMap
    private targetFromList: INodeInfo; // from targetList

    dataFetchFailedText: string;
    dataEmptyText: string;
    chartConfig: IChartConfig;
    showLoading: boolean;
    showRetry: boolean;
    retryMessage: string;
    chartVisibility = {};
    previousRange: number[];

    constructor(
        private injector: Injector,
        private translateService: TranslateService,
        private webAppSettingDataService: WebAppSettingDataService,
        private analyticsService: AnalyticsService,
        private dynamicPopupService: DynamicPopupService,
        private componentFactoryResolver: ComponentFactoryResolver,
        private storeHelperService: StoreHelperService,
        private messageQueueService: MessageQueueService,
        private agentHistogramDataService: AgentHistogramDataService,
        private newUrlStateNotificationService: NewUrlStateNotificationService,
        private cd: ChangeDetectorRef,
    ) {
    }

    ngOnInit() {
        this.initChartColors();
        this.initI18nText();
        this.listenToEmitter();
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    set activeLayer(layer: Layer) {
        this.showLoading = layer === Layer.LOADING;
        this.showRetry = layer === Layer.RETRY;
        this.setChartVisibility(layer === Layer.CHART);
        this.cd.markForCheck();
    }

    onRendered(): void {
        this.activeLayer = Layer.CHART;
    }

    private setChartVisibility(showChart: boolean): void {
        this.chartVisibility = {
            'show-chart': showChart,
            'shady-chart': !showChart && this.chartConfig !== undefined,
        };
    }

    onRetry(): void {
        this.activeLayer = Layer.LOADING;
        const target = this.getTargetInfo();

        this.agentHistogramDataService.getData(this.serverMapData, this.previousRange, target).pipe(
            pluck('agentTimeSeriesHistogram', this.selectedAgent),
            catchError((error: IServerError) => {
                this.activeLayer = Layer.RETRY;
                this.dynamicPopupService.openPopup({
                    data: {
                        title: 'Error',
                        contents: error
                    },
                    component: ServerErrorPopupContainerComponent
                }, {
                    resolver: this.componentFactoryResolver,
                    injector: this.injector
                });

                return EMPTY;
            })
        ).pipe(
            map((data: IHistogram[]) => this.cleanStatisticsChartData(data)),
            map((data: IHistogram[]) => this.makeChartData(data)),
            withLatestFrom(this.storeHelperService.getLoadChartYMax(this.unsubscribe))
        ).subscribe(([chartData, yMax]: [PrimitiveArray[], number]) => {
            this.chartConfig = {
                dataConfig: this.makeDataOption(chartData),
                elseConfig: this.makeElseOption(yMax)
            };
        });

        this.cd.markForCheck();
    }

    private initChartColors(): void {
        this.chartColors = this.webAppSettingDataService.getColorByRequest();
    }

    private initI18nText(): void {
        forkJoin(
            this.translateService.get('COMMON.FAILED_TO_FETCH_DATA'),
            this.translateService.get('COMMON.NO_DATA'),
        ).subscribe(([dataFetchFailedText, dataEmptyText]: string[]) => {
            this.dataFetchFailedText = dataFetchFailedText;
            this.dataEmptyText = dataEmptyText;
        });
    }

    private listenToEmitter(): void {
        this.newUrlStateNotificationService.onUrlStateChange$.pipe(
            takeUntil((this.unsubscribe)),
        ).subscribe(() => {
            this.serverMapData = null;
            this.originalTarget = null;
            this.targetFromList = null;
        });

        this.storeHelperService.getTimezone(this.unsubscribe).subscribe((timezone: string) => {
            this.timezone = timezone;
        });

        this.storeHelperService.getDateFormatArray(this.unsubscribe, 6, 7).subscribe(([dateFormatMonth, dateFormatDay]: string[]) => {
            this.dateFormatMonth = dateFormatMonth;
            this.dateFormatDay = dateFormatDay;
        });

        merge(
            this.messageQueueService.receiveMessage(this.unsubscribe, MESSAGE_TO.SERVER_MAP_TARGET_SELECT).pipe(
                filter(() => this.sourceType !== SourceType.INFO_PER_SERVER),
                filter((target: ISelectedTarget) => {
                    this.isOriginalNode = true;
                    this.selectedAgent = '';
                    this.originalTarget = target;
                    this.targetFromList = null;

                    return !target.isMerged;
                }),
                map(() => this.getTargetInfo().timeSeriesHistogram),
            ),
            this.storeHelperService.getAgentSelection(this.unsubscribe).pipe(
                filter(() => this.sourceType !== SourceType.INFO_PER_SERVER),
                filter((agent: string) => this.selectedAgent !== agent),
                tap((agent: string) => this.selectedAgent = agent),
                filter(() => !!this.originalTarget),
                map(() => this.getTargetInfo()),
                switchMap((target: INodeInfo) => {
                    if (this.isAllAgent()) {
                        return of(target.timeSeriesHistogram);
                    } else {
                        let data;

                        if (this.sourceType === SourceType.MAIN) {
                            const range = this.previousRange = this.newUrlStateNotificationService.isRealTimeMode()
                                ? this.previousRange ? this.previousRange : [this.newUrlStateNotificationService.getStartTimeToNumber(), this.newUrlStateNotificationService.getEndTimeToNumber()]
                                : [this.newUrlStateNotificationService.getStartTimeToNumber(), this.newUrlStateNotificationService.getEndTimeToNumber()];

                            data = this.agentHistogramDataService.getData(this.serverMapData, range, target).pipe(
                                catchError(() => of(null)),
                                filter((res: any) => res === null ? (this.activeLayer = Layer.RETRY, false) : true)
                            );
                        } else {
                            data = of(target);
                        }

                        return data.pipe(
                            pluck('agentTimeSeriesHistogram', this.selectedAgent)
                        );
                    }
                }),
            ),
            this.messageQueueService.receiveMessage(this.unsubscribe, MESSAGE_TO.SERVER_MAP_TARGET_SELECT_BY_LIST).pipe(
                filter(() => this.sourceType !== SourceType.INFO_PER_SERVER),
                tap((target: any) => {
                    this.selectedAgent = '';
                    this.targetFromList = target;
                    if (this.originalTarget.isNode) {
                        // this.isOriginalNode = this.originalTarget.node.includes(target.key);
                        this.isOriginalNode = true;
                    } else {
                        this.isOriginalNode = true;
                    }
                }),
                map((target: any) => target.timeSeriesHistogram)
            ),
            this.messageQueueService.receiveMessage(this.unsubscribe, MESSAGE_TO.AGENT_SELECT_FOR_SERVER_LIST).pipe(
                filter(() => this.sourceType === SourceType.INFO_PER_SERVER),
                tap(({agent}: IAgentSelection) => this.selectedAgent = agent),
                pluck('load'),
            ),
            this.messageQueueService.receiveMessage(this.unsubscribe, MESSAGE_TO.SERVER_MAP_DATA_UPDATE).pipe(
                tap(({serverMapData, range}: { serverMapData: ServerMapData, range: number[] }) => {
                    this.serverMapData = serverMapData;
                    this.previousRange = range;
                }),
                filter(() => !!this.originalTarget),
                filter(() => this.sourceType === SourceType.MAIN),
                filter(() => !this.originalTarget.isMerged || !!this.targetFromList),
                switchMap(({serverMapData, range}: { serverMapData: ServerMapData, range: number[] }) => {
                    const target = this.originalTarget.isMerged || this.targetFromList ? serverMapData.getNodeData(this.targetFromList.key) || serverMapData.getLinkData(this.targetFromList.key)
                        : this.getTargetInfo();

                    return !target ? of(null)
                        : this.selectedAgent ? this.agentHistogramDataService.getData(serverMapData, range, target).pipe(
                                catchError(() => of(null)),
                                filter((res: any) => !!res),
                                pluck('agentTimeSeriesHistogram', this.selectedAgent)
                            )
                            : of(target.timeSeriesHistogram);
                }),
            )
        ).pipe(
            map((data: IHistogram[]) => this.cleanStatisticsChartData(data)),
            map((data) => this.makeChartData(data)),
            switchMap((data: PrimitiveArray[]) => {
                if (this.shouldUpdateYMax()) {
                    const maxTickValue = getMaxTickValue(getStackedData(data), 1);
                    const yMax = maxTickValue === 0 ? this.defaultYMax : maxTickValue;

                    this.storeHelperService.dispatch(new Actions.UpdateLoadChartYMax(yMax));

                    return of([data, yMax]);
                } else {
                    return of(data).pipe(
                        withLatestFrom(this.storeHelperService.getLoadChartYMax(this.unsubscribe))
                    );
                }
            }),
        ).subscribe(([chartData, yMax]: [PrimitiveArray[], number]) => {
            this.chartConfig = {
                dataConfig: this.makeDataOption(chartData),
                elseConfig: this.makeElseOption(yMax),
            };

            this.cd.markForCheck();
        });
    }

    private shouldUpdateYMax(): boolean {
        return this.isAllAgent() && this.isOriginalNode;
    }

    private isAllAgent(): boolean {
        return this.selectedAgent === '';
    }

    private getTargetInfo(): any {
        return this.originalTarget.isNode
            ? this.serverMapData.getNodeData(this.originalTarget.node[0])
            : this.serverMapData.getLinkData(this.originalTarget.link[0]);
    }

    private cleanStatisticsChartData(data: IHistogram[]): IHistogram[] {
        const excludes = ['Sum', 'Tot', 'Avg', 'Max'];

        return data ? data.filter(({key}: IHistogram) => excludes.indexOf(key) === -1) : null;
    }

    private makeChartData(data: IHistogram[]): PrimitiveArray[] {
        return data
            ? [
                ['x', ...data[0].values.map(([timeStamp]: number[]) => timeStamp)],
                ...data.map(({key, values}: IHistogram) => [key, ...values.map(([, value]: number[]) => value)])
            ]
            : [];
    }

    private makeDataOption(columns: PrimitiveArray[]): Data {
        const keyList = columns.slice(1).map(([key]: PrimitiveArray) => key as string);

        return {
            x: 'x',
            columns,
            empty: {
                label: {
                    text: this.dataEmptyText
                }
            },
            type: areaStep(),
            colors: keyList.reduce((acc: { [key: string]: string }, curr: string, i: number) => {
                return {...acc, [curr]: this.chartColors[i]};
            }, {}),
            groups: [keyList],
            order: null
        };
    }

    private makeElseOption(yMax: number): { [key: string]: any } {
        return {
            padding: {
                top: 20,
                bottom: 10,
                right: 25
            },
            axis: {
                x: {
                    type: 'timeseries',
                    tick: {
                        count: 6,
                        show: false,
                        format: (time: Date) => {
                            return moment(time).tz(this.timezone).format(this.dateFormatMonth) + '\n' + moment(time).tz(this.timezone).format(this.dateFormatDay);
                        }
                    },
                    padding: {
                        left: 0,
                        right: 0
                    }
                },
                y: {
                    tick: {
                        count: 3,
                        format: (v: number): string => this.convertWithUnit(v)
                    },
                    padding: {
                        top: 0,
                        bottom: 0
                    },
                    min: 0,
                    max: yMax,
                    default: [0, this.defaultYMax]
                }
            },
            grid: {
                y: {
                    show: true,
                }
            },
            point: {
                show: false,
            },
            tooltip: {
                order: '',
                format: {
                    value: (v: number) => this.addComma(v.toString())
                }
            },
            transition: {
                duration: 0
            }
        };
    }

    private addComma(str: string): string {
        return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
    }

    private convertWithUnit(value: number): string {
        const unitList = ['', 'K', 'M', 'G'];

        return [...unitList].reduce((acc: string, curr: string, i: number, arr: string[]) => {
            const v = Number(acc);

            return v >= 1000
                ? (v / 1000).toString()
                : (arr.splice(i + 1), Number.isInteger(v) ? `${v}${curr}` : `${v.toFixed(2)}${curr}`);
        }, value.toString());
    }

    onClickColumn($event: string): void {
        this.analyticsService.trackEvent(TRACKED_EVENT_LIST.CLICK_LOAD_GRAPH);
    }

    onShowHelp($event: MouseEvent): void {
        this.analyticsService.trackEvent(TRACKED_EVENT_LIST.TOGGLE_HELP_VIEWER, HELP_VIEWER_LIST.LOAD);
        const {left, top, width, height} = ($event.target as HTMLElement).getBoundingClientRect();

        this.dynamicPopupService.openPopup({
            data: HELP_VIEWER_LIST.LOAD,
            coord: {
                coordX: left + width / 2,
                coordY: top + height / 2
            },
            component: HelpViewerPopupContainerComponent
        }, {
            resolver: this.componentFactoryResolver,
            injector: this.injector
        });
    }
}
