<template>
    <table-pannel ref="tablePannel" @on-cancel="onCancel" :show-ok-button="showOkButton">
        <slot slot="alert">
            <Alert show-icon>告警中心：告警中心统一管理，支持告警信息、处理信息查看、删除告警信息</Alert>
        </slot>
        <slot slot="filter">
            <table>
                <tr>
                    <td>
                        <Select filterable clearable @on-change="onSearch" v-model="modelTable.filter.deviceCode"
                            placeholder="请选择设备查询" style="width:260px">
                            <Option v-for="item in deviceList" :value="item.deviceCode" :key="item.deviceCode"
                                :label="item.deviceName">
                            </Option>
                        </Select>
                    </td>
                    <td>
                        <Select clearable placeholder="请选择处理状态查询" @on-change="onSearch"
                            v-model="modelTable.filter.warningHandleState" style="width:180px">
                            <Option v-for="item in dict.warningHandleType" :value="item.dictCode" :key="item.dictCode"
                                :label="item.dictName">
                            </Option>
                        </Select>
                    </td>
                    <td>
                        <Select clearable placeholder="请选择告警类型查询" @on-change="onSearch"
                            v-model="modelTable.filter.warningState" style="width:180px">
                            <Option v-for="item in dict.warningState" :value="item.dictCode" :key="item.dictCode"
                                :label="item.dictName">
                            </Option>
                        </Select>
                    </td>
                </tr>

            </table>
        </slot>
        <slot slot="action">

            <table>
                <tr>

                    <td>
                        <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteBatchWarning">
                            <Button type="error" ghost :disabled="this.modelTable.select<=0">批量删除</Button>
                        </Poptip>
                    </td>

                </tr>
            </table>
        </slot>
        <slot slot="summary">总计：筛选符合条件记录：{{modelTable.paging.total}} 条</slot>
        <slot slot="grid">

            <vxe-table ref="modelTable" border stripe resizable highlight-current-row show-header-overflow show-overflow
                highlight-hover-row :loading="modelTable.loading" :data="modelTable.data"
                @checkbox-change="onTableCheckChange">
                <vxe-table-column type="checkbox" width="60" align="center"></vxe-table-column>
                <vxe-table-column field="deviceName" width="160" title="告警设备名称"></vxe-table-column>
                <vxe-table-column field="deviceCode" width="130" title="告警设备编号"></vxe-table-column>
                <vxe-table-column field="warningTime" align="center" title="告警时间"></vxe-table-column>
                <vxe-table-column field="warningStateName" width="180" align="center" title="告警类型"></vxe-table-column>
                <vxe-table-column field="warningValue" align="right" title="告警值">
                    <template v-slot="{ row }">
                        <span v-if="row.warningValue==32767.00">未连接</span>
                        <span v-else>{{row.warningValue}}</span>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="warningHandelStateName" title="告警处理状态" align="center">
                    <template v-slot="{ row }">
                        <Tag v-if="row.warningHandelState=='1'" color="warning">待处理</Tag>
                        <Tag v-if="row.warningHandelState=='2'" color="primary">处理中</Tag>
                        <Tag v-if="row.warningHandelState=='3'" color="success">已处理</Tag>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="action" width="150" title="操作" align="center">
                    <template v-slot="{ row }">
                        <a href="javascript:void(0)" @click="onViewWarningHandle(row)">
                            处理信息
                        </a>
                        <div class="split"></div>
                        <Dropdown transfer>
                            <a href="javascript:void(0)">
                                更多
                                <Icon type="ios-arrow-down"></Icon>
                            </a>
                            <DropdownMenu slot="list">
                                <div>
                                    <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteWarning(row)">
                                        <DropdownItem>删除告警信息</DropdownItem>
                                    </Poptip>
                                </div>
                            </DropdownMenu>
                        </Dropdown>
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
        WARNING_LIST_SERVICE,
        WARNING_DELETE_SERVICE,
        WARNING_DELETE_BATCH_SERVICE,
        WARNING_BATCH_SERVICE,
        DICT_LIST_BY_DICT_TYPE_CODE_SERVICE,
        DEVICE_ALL_LIST_SERVICE
    } from "../../../axios/api";
    import {
        Poptip
    } from 'view-design';

    import busWarningProcess from './busWarningProcess';

    export default {
        components: {
            tablePannel,
            busWarningProcess
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
                        deviceCode: "",
                        warningState: "",
                        warningHandleState: ""
                    },
                    loading: false
                },

                currentComponent: "",
                currentComponentKey: "",

                showOkButton: false,

                deviceList: [],

                dict: {
                    warningHandleType: [],
                    warningState: []
                }

            }
        },
        mounted() {
            this.getDict();
            this.onSearch();
            this.getDeviceList();
        },
        methods: {
            onSearch() {
                this.resetPage();
                this.getWarningList();
            },
            getDict() {

                var vm = this;

                DICT_LIST_BY_DICT_TYPE_CODE_SERVICE({
                    dictTypeCode: "warningHandleType"
                }).then(response => {
                    if (response.success) {
                        vm.dict.warningHandleType = response.result;
                    };
                });

                DICT_LIST_BY_DICT_TYPE_CODE_SERVICE({
                    dictTypeCode: "warningState"
                }).then(response => {
                    if (response.success) {
                        vm.dict.warningState = response.result;
                    };
                });

            },
            getWarningList() {

                var vm = this;

                this.modelTable.loading = true;

                WARNING_LIST_SERVICE(Object.assign(this.modelTable.filter, {

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
            onViewWarningHandle(warning) {

                this.showOkButton = false;

                this.currentComponent = busWarningProcess;
                this.currentComponentKey = warning.id;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "处理信息（" + (warning.deviceName||warning.deviceCode) + "）";

                this.modelForm.id = warning.id;

            },
            onCancel() {

                this.currentComponent = null;
                this.currentComponentKey = null;

                this.$refs.tablePannel.showDrawer = false;
                this.$refs.tablePannel.showLoading = false;

                this.modelForm.id = null;

            },
            onDeleteWarning(warning) {
                WARNING_DELETE_SERVICE({
                    id: warning.id,
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getWarningList();
                    }
                });
            },
            onDeleteBatchWarning() {

                var ids = [];
                var warningList = this.$refs.modelTable.getCheckboxRecords();
                _.forEach(warningList, function (warning) {
                    ids.push(warning.id)
                });

                WARNING_DELETE_BATCH_SERVICE({
                    ids: _.join([ids], ','),
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getWarningList();
                    }
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
            onTableCheckChange() {
                this.modelTable.select = this.$refs.modelTable.getCheckboxRecords();
            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getWarningList();
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getWarningList();
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


    table {
        td {
            padding: 0px 5px;
        }
    }
</style>