<template>
    <div id="analysis-layout">

        <div class="analysis-header-pannel">
            <div class="analysis-header-l">
            </div>
            <div class="analysis-header-l-line">
            </div>
            <div class="analysis-header-title">
                智能接地箱综合展示分析平台
            </div>
            <div class="analysis-header-r-line">
            </div>
            <div class="analysis-header-r">
            </div>
        </div>
        <div class="analysis-row">
            <div class="analysis-col-side">

                <card-analysis-pannel title="数据汇总" style="height: 180px;">
                    告警数据量+监测数据量
                </card-analysis-pannel>
                <card-analysis-pannel title="在线统计" style="height: 300px;">
                    截至2020年7月7日，在线设备累计154台，离线设备类型65台，离线设备占比50%；
                </card-analysis-pannel>
                <card-analysis-pannel title="设备清单" style="height: calc(100% - 512px);">
                    设备列表 支持查看详情
                </card-analysis-pannel>

            </div>
            <div class="analysis-col-main">

                <card-analysis-pannel class="analysis-col-main-map"></card-analysis-pannel>

                <div class="analysis-col-main-bottom">

                    <card-analysis-pannel title="实时数据" style="height: 100%;width: 100%;">
                        <vxe-table height="295" border="inner" stripe ref="modelTable" resizable highlight-current-row
                            show-header-overflow show-overflow highlight-hover-row :loading="modelTable.loading"
                            :data="modelTable.data">
                            <vxe-table-column field="deviceName" width="300" title="设备名称">
                            </vxe-table-column>
                            <vxe-table-column width="3000" align="left" title="监测项目">
                                <template v-slot="{ row }">
                                    <div class="point-pannel">
                                        <div class="point-pannel-warp" v-for="(item,index) in row.pointNameList"
                                            :key="index">
                                            <div class="point-pannel-warp-content">
                                                <div>
                                                    <label class="point-pannel-warp-content-tag">●</label>
                                                    <label class="point-pannel-warp-content-point">{{item}}</label>
                                                </div>
                                                <div>
                                                    <label
                                                        class="point-pannel-warp-content-time">{{row.monitorTimeList[index]}}</label>
                                                </div>
                                            </div>
                                            <div class="point-pannel-warp-content">
                                                <div>
                                                    <label
                                                        class="point-pannel-warp-content-value">{{row.monitorValueList[index]}}</label>
                                                </div>
                                                <div>
                                                    <label
                                                        class="point-pannel-warp-content-unit">{{row.pointUnitList[index]}}</label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </template>
                            </vxe-table-column>
                        </vxe-table>
                    </card-analysis-pannel>

                </div>

            </div>
            <div class="analysis-col-side">
                <card-analysis-pannel title="实时告警" style="height: 525px;">
                    实时滚动告警清单（未处理）
                </card-analysis-pannel>
                <card-analysis-pannel title="告警分析" style="height: calc(100% - 548px);">
                    截至2020年7月7日，待处理告警568条，处理中658条，已处理584条。

                    人员处理柱状图
                </card-analysis-pannel>
            </div>
        </div>
    </div>

</template>
<script>
    import cardAnalysisPannel from '../../components/card-analysis-pannel/index';
    import cardAnalysisMapPannel from '../../components/card-analysis-map-pannel/index';

    import {
        MONITOR_LIST_SERVICE,
        DEVICE_ALL_LIST_SERVICE
    } from "../../axios/api";

    export default {
        components: {
            cardAnalysisPannel,
            cardAnalysisMapPannel
        },
        data() {
            return {

                modelForm: {
                    id: ""
                },
                modelTable: {
                    data: [],
                    paging: {
                        pageSize: 10000,
                        pageIndex: 1,
                        total: 0
                    },
                    select: [],
                    filter: {
                        deviceCode: null,
                        keyword: ""
                    },
                    loading: false
                },

            }
        },
        mounted() {
            this.onSearch();
            this.getDeviceList();
        },
        methods: {
            onSearch() {
                this.resetPage();
                this.getMonitorRealList();
            },
            getMonitorRealList() {

                var vm = this;

                this.modelTable.loading = true;

                MONITOR_LIST_SERVICE(Object.assign(this.modelTable.filter, {

                    pageNo: this.modelTable.paging.pageIndex,
                    pageSize: this.modelTable.paging.pageSize

                })).then(response => {

                    var result = response.result;

                    if (!_.isNil(result)) {
                        vm.modelTable.data = result.records || [];
                        vm.modelTable.paging.total = result.total;
                    };

                    vm.modelTable.loading = false;

                });

            },
            getDeviceList() {

                var vm = this;

                DEVICE_ALL_LIST_SERVICE({}).then(response => {

                    var result = response.result;

                    if (!_.isNil(result)) {
                        vm.deviceList = result;
                    };

                });

            },
            onViewHistoryList(device) {

                this.showOkButton = false;

                this.currentComponent = busMonitorHistoryList;
                this.currentComponentKey = device.deviceCode;

                this.$refs.tablePannel.drawerTitle = "历史数据（" + device.deviceName + "）";
                this.$refs.tablePannel.showDrawer = true;

                this.modelForm.id = device.deviceCode;
            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getMonitorRealList();
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getMonitorRealList();
            },
            resetPage: function () {
                this.modelTable.paging.pageIndex = 1;
                this.modelTable.paging.pageSize = 10;
            }

        }
    };
</script>
<style lang="less">
    @import "./busAnalysis.less";

    .analysis-col-main-bottom {
        .vxe-header--column {

            background-repeat: no-repeat;
            background-size: 1px 100%, 100% 1px;
            background-position: 100% 0, 100% 100%;
            background-color: rgba(8, 61, 99, 0.25);


        }

        .vxe-table.border--default .vxe-table--header-wrapper,
        .vxe-table.border--full .vxe-table--header-wrapper,
        .vxe-table.border--outer .vxe-table--header-wrapper {
            background-color: rgba(8, 61, 99, 0.25);
        }

        .vxe-table .vxe-body--row {
            background-color: rgba(8, 61, 99, 0.25);
        }

        .vxe-table--fixed-left-wrapper {
            background-color: rgba(8, 61, 99, 0.25);
        }

        .vxe-table--fixed-right-wrapper {
            background-color: rgba(8, 61, 99, 0.25);
        }

        .vxe-table .vxe-table--body,
        .vxe-table .vxe-table--footer,
        .vxe-table .vxe-table--header {
            background-color: rgba(8, 61, 99, 0.25);
        }

        .vxe-checkbox .vxe-checkbox--label,
        .vxe-radio-button .vxe-radio--label,
        .vxe-radio .vxe-radio--label,
        .vxe-table .vxe-body--column.col--ellipsis:not(.col--actived) .vxe-cell,
        .vxe-table .vxe-footer--column.col--ellipsis:not(.col--actived) .vxe-cell {

            color: #1FE4F7;
        }

        .vxe-table.border--inner .vxe-table--header-wrapper,
        .vxe-table.border--none .vxe-table--header-wrapper {
            background: none;
        }


        .vxe-table.border--default .vxe-body--column,
        .vxe-table.border--default .vxe-footer--column,
        .vxe-table.border--default .vxe-header--column,
        .vxe-table.border--inner .vxe-body--column,
        .vxe-table.border--inner .vxe-footer--column,
        .vxe-table.border--inner .vxe-header--column {
            background-image: linear-gradient(#3DB4DD, #3DB4DD);
            background-repeat: no-repeat;
            background-size: 100% 1px;
            background-position: 100% 100%;
        }

    }
</style>