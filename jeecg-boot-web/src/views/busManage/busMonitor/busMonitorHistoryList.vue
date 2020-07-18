<template>
    <table-pannel ref="tablePannel" :show-ok-button="false">
        <slot slot="alert">
            <Alert show-icon>历史数据：显示设备所有历史监测数据</Alert>
        </slot>
        <slot slot="filter">
            <table class="treeselect">
                <tr>
                    <td>
                        <DatePicker type="daterange" @on-change="onDatePickerChange" placeholder="请选择时间范围查询"
                            style="width: 300px"></DatePicker>
                    </td>
                </tr>
            </table>
        </slot>
        <slot slot="action">
            <table>
                <tr>
                    <td>
                        <Button icon="md-refresh" type="primary" @click="onRefresh">刷新</Button>
                    </td>

                </tr>
            </table>
        </slot>
        <slot slot="summary">总计：筛选符合条件记录：{{modelTable.paging.total}} 条</slot>
        <slot slot="grid">

            <vxe-table ref="modelTable" border stripe resizable highlight-current-row show-header-overflow show-overflow
                highlight-hover-row :loading="modelTable.loading" :data="modelTable.data">
                <vxe-table-column field="monitorTime" width="180" fixed="left" align="center" title="监测时间">
                </vxe-table-column>
                <vxe-table-column width="8000" align="left" title="监测项目">
                    <template v-slot="{ row }">
                        <div class="point-pannel">
                            <div class="point-pannel-warp" v-for="(item,index) in row.tbusElectricDeviceDatRealVOList"
                                :key="index">
                                <div class="point-pannel-warp-content">
                                    <div>
                                        <template v-if="item.monitorValue==32767.00">
                                            <label class="point-pannel-warp-content-tag-offline">●</label>
                                        </template>
                                        <template v-else>
                                            <label class="point-pannel-warp-content-tag">●</label>
                                        </template>

                                        <label class="point-pannel-warp-content-point">{{item.pointName}}</label>
                                    </div>

                                </div>
                                <div class="point-pannel-warp-content">
                                    <div>
                                        <template v-if="item.monitorValue==32767.00">
                                            <label class="point-pannel-warp-content-no-value">未连接</label>
                                        </template>
                                        <template v-else>
                                            <label class="point-pannel-warp-content-value">{{item.monitorValue}}</label>
                                        </template>

                                    </div>
                                    <div>
                                        <label class="point-pannel-warp-content-unit">{{item.pointUnit}}</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </template>
                </vxe-table-column>
            </vxe-table>

        </slot>
        <slot slot="page">
            <Page :page-size-opts="[10, 20, 50, 100]" :total="modelTable.paging.total"
                :current="modelTable.paging.pageIndex" :page-size="modelTable.paging.pageSize" @on-change="onPageChange"
                @on-page-size-change="onPageSizeChange" show-total show-sizer show-elevator />
        </slot>
    </table-pannel>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import tablePannel from '../../../components/table-pannel';
    import {
        MONITOR_HIS_LIST_SERVICE
    } from "../../../axios/api";
    import {
        Poptip
    } from 'view-design';

    export default {
        name: "busMonitorHistoryList",
        components: {
            tablePannel
        },
        props: {
            id: {
                type: String
            },
        },
        data() {
            return {

                modelForm: {
                    id: ""
                },

                modelTable: {
                    data: [],
                    paging: {
                        pageSize: 10,
                        pageIndex: 1,
                        total: 0
                    },
                    filter: {
                        deviceCode: "",
                        statusCode: "",
                        monitorStartTime: "",
                        monitorEndTime: "",
                        keyword: ""
                    },
                    loading: false
                },


                ref: ""
            }
        },
        mounted() {

            this.onSearch();

        },
        methods: {
            onSearch() {

                this.modelTable.filter.deviceCode = this.id;

                this.resetPage();
                this.getMonitorHistoryList(true);

            },
             onRefresh() {
                this.resetPage();
                this.getMonitorHistoryList(false);
            },
            getMonitorHistoryList(cache) {

                var vm = this;

                this.modelTable.loading = true;

                MONITOR_HIS_LIST_SERVICE(Object.assign(this.modelTable.filter, {

                    pageNo: this.modelTable.paging.pageIndex,
                    pageSize: this.modelTable.paging.pageSize,
                    cache: cache 

                })).then(response => {

                    var result = response.result;

                    if (!_.isNil(result)) {
                        vm.modelTable.data = result.records || [];
                        vm.modelTable.paging.total = result.total;
                    };

                    this.modelTable.loading = false;

                });
            },
            onDatePickerChange(value) {

                this.modelTable.filter.monitorStartTime = value[0];
                this.modelTable.filter.monitorEndTime = value[1];
                this.onSearch();
            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getMonitorHistoryList(true);
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getMonitorHistoryList(true);
            },
            resetPage: function () {
                this.modelTable.data = [];
                this.modelTable.paging.pageIndex = 1;
                this.modelTable.paging.pageSize = 10;
            }
        }
    };
</script>
<style lang="less" scoped>
    @import "../../../style/common.less";

    .point-pannel {
        display: flex;
        flex-direction: row;

        .point-pannel-warp {
            width: 200px;
            height: 48px;
            border-right: 1px solid rgb(220, 220, 220);

            .point-pannel-warp-content {
                display: flex;
                justify-content: space-between;
                padding-left: 6px;

                .point-pannel-warp-content-point {
                    color: black;
                    font-size: 14px;

                }

                .point-pannel-warp-content-tag {
                    color: rgb(0, 169, 80);
                    font-family: 宋体;
                }

                .point-pannel-warp-content-tag-offline {
                    color: #FF9900;
                    font-family: 宋体;
                }

                .point-pannel-warp-content-time {
                    padding-right: 8px;
                    padding-left: 8px;
                    color: rgb(101, 113, 128);
                    font-size: 12px;
                }

                .point-pannel-warp-content-value {
                    color: black;
                    font-size: 15px;
                }

                .point-pannel-warp-content-no-value {
                    color: #606266;
                    font-size: 13px;
                }

                .point-pannel-warp-content-unit {
                    padding-right: 5px;
                    color: rgb(101, 113, 128);
                    font-size: 12px;
                }
            }


        }
    }


    table {
        td {
            padding: 0px 5px;

        }
    }
</style>