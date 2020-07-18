<template>
    <table-pannel ref="tablePannel" :show-ok-button="showOkButton" @on-cancel="onCancel" :drawer-width="1100">
        <slot slot="alert">
            <Alert show-icon>实时数据：显示各个设备现场最新监测数据</Alert>
        </slot>
        <slot slot="filter">
            <table class="treeselect">
                <tr>
                    <td>

                        <Select filterable clearable @on-change="onSearch" v-model="modelTable.filter.deviceCode"
                            placeholder="请选择设备查询" style="width:260px">
                            <Option v-for="item in deviceList" :value="item.deviceCode" :key="item.deviceCode"
                                :label="item.deviceName">
                            </Option>
                        </Select>
                    </td>
                    <!-- <td>
                        <Divider type="vertical" />
                    </td>
                    <td>
                        <Input suffix="ios-search" @on-change="onSearch" v-model="modelTable.filter.keyword"
                            placeholder="请输入关键字查询" style="width: auto" />
                    </td> -->
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
                <vxe-table-column field="deviceName" width="200" fixed="left" title="设备名称"></vxe-table-column>
                <vxe-table-column field="monitorTime" width="160" fixed="left" align="center" title="最新监测时间">
                </vxe-table-column>
                <vxe-table-column width="10000" align="left" title="监测项目">
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
                <vxe-table-column field="action" fixed="right" width="100" title="操作" align="center">
                    <template v-slot="{ row }">
                        <a href="javascript:void(0)" @click="onViewHistoryList(row)">
                            历史数据
                        </a>
                    </template>
                </vxe-table-column>
            </vxe-table>

        </slot>
        <slot slot="page">
            <Page :page-size-opts="[10, 20, 50, 100]" :total="modelTable.paging.total"
                :current="modelTable.paging.pageIndex" :page-size="modelTable.paging.pageSize" @on-change="onPageChange"
                @on-page-size-change="onPageSizeChange" show-total show-sizer show-elevator />
        </slot>
        <slot slot="drawer">
            <component ref="drawerComponent" :key="currentComponentKey" :is="currentComponent" :id="modelForm.id">
            </component>
        </slot>
    </table-pannel>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import tablePannel from '../../../components/table-pannel';
    import {
        MONITOR_LIST_SERVICE,
        DEVICE_ALL_LIST_SERVICE
    } from "../../../axios/api";
    import {
        Poptip
    } from 'view-design';

    import busMonitorHistoryList from './busMonitorHistoryList';

    export default {
        components: {
            tablePannel,
            busMonitorHistoryList
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
                    select: [],
                    filter: {
                        deviceCode: null,
                        keyword: ""
                    },
                    loading: false
                },

                currentComponent: "",
                currentComponentKey: "",

                showOkButton: false,

                deviceList: []

            }
        },
        mounted() {
            this.onSearch();
            this.getDeviceList();
        },
        methods: {
            onSearch() {
                this.resetPage();
                this.getMonitorRealList(true);
            },
            onRefresh() {
                this.resetPage();
                this.getMonitorRealList(false);
            },
            getMonitorRealList(cache) {

                var vm = this;

                this.modelTable.loading = true;

                MONITOR_LIST_SERVICE(Object.assign(this.modelTable.filter, {

                    pageNo: this.modelTable.paging.pageIndex,
                    pageSize: this.modelTable.paging.pageSize,
                    cache: cache 

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
            onCancel() {

                this.currentComponent = null;
                this.currentComponentKey = null;

                this.$refs.tablePannel.showDrawer = false;

                this.modelForm.id = null;

            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getMonitorRealList(true);
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getMonitorRealList(true);
            },
            resetPage: function () {
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