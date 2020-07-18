<template>
    <table-pannel ref="tablePannel" @on-ok="onOk" @on-cancel="onCancel" :show-ok-button="showOkButton">
        <slot slot="alert">
            <Alert show-icon>设备管理：设备统一管理，支持设备的增删改查、设备监测项目动态维护、设备所属机构维护</Alert>
        </slot>
        <slot slot="filter">
            <table class="treeselect">
                <tr>
                     <td>
                        <treeselect :disable-branch-nodes="true" :show-count="true" :multiple="false"
                            :options="orgTreeList" :normalizer="orgTreeNormalizer" @input="onSearch"
                            placeholder="请选择设备所属机构查询" v-model="modelTable.filter.orgId" style="width: 200px"/>
                    </td>
                    <td>
                        <Input  suffix="ios-search" @on-change="onSearch" v-model="modelTable.filter.keyword"
                            placeholder="请输入关键字查询" style="width: auto" />
                    </td>
                </tr>
            </table>
        </slot>
        <slot slot="action">

            <table>
                <tr>
                    <td>
                        <Button type="primary" @click="onAddDevice">添加设备</Button>
                    </td>
                    <td>
                        <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteBatchDevice">
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
                <vxe-table-column type="checkbox" width="60" fixed="left" align="center"></vxe-table-column>
                <vxe-table-column field="deviceName" width="150" fixed="left" title="设备名称"></vxe-table-column>
                <vxe-table-column field="deviceCode" width="150" align="center" title="设备出厂编号"></vxe-table-column>
                <vxe-table-column field="deviceManufacturer" width="150" title="设备制造厂"></vxe-table-column>
                <vxe-table-column field="deviceModel" width="150" title="规格型号"></vxe-table-column>
                <vxe-table-column field="devicePower" width="150" title="设备电源"></vxe-table-column>
                <vxe-table-column field="deviceRate" width="150" title="设备功率"></vxe-table-column>
                <vxe-table-column field="deviceFactoryDate" width="150" align="center" title="设备出厂日期">
                </vxe-table-column>
                <vxe-table-column field="deviceLine" width="150" title="设备安装线路"></vxe-table-column>
                <vxe-table-column field="deviceInstall" width="150" title="设备安装用户"></vxe-table-column>
                <vxe-table-column field="deviceInstallDate" width="150" align="center" title="设备安装日期">
                </vxe-table-column>
                <vxe-table-column field="longitude" width="150" align="right" title="设备安装经度"></vxe-table-column>
                <vxe-table-column field="latitude" width="150" align="right" title="设备安装纬度"></vxe-table-column>
                <vxe-table-column field="statusCode" width="80" fixed="right" title="启用状态" align="center">
                    <template v-slot="{ row }">
                        <Switch :value="row.statusCode=='0'?true:false" @on-change="onBatchDevice(row)" />
                    </template>
                </vxe-table-column>
                <vxe-table-column field="action" fixed="right" width="150" title="操作" align="center">
                    <template v-slot="{ row }">
                        <a href="javascript:void(0)" @click="onEditDevice(row)">
                            编辑
                        </a>
                        <div class="split"></div>
                        <Dropdown transfer>
                            <a href="javascript:void(0)">
                                更多
                                <Icon type="ios-arrow-down"></Icon>
                            </a>
                            <DropdownMenu slot="list">
                                <div @click="onBindDevicePoint(row)">
                                    <DropdownItem>设置监测项目</DropdownItem>
                                </div>
                            </DropdownMenu>
                            <DropdownMenu slot="list">
                                <div>
                                    <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteDevice(row)">
                                        <DropdownItem>删除设备</DropdownItem>
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
            <component ref="drawerComponent" :key="currentComponentKey" :is="currentComponent"
                @on-save-error="onSaveError" @on-save-success="onSaveSuccess" :id="modelForm.id"></component>
        </slot>
    </table-pannel>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import tablePannel from '../../../components/table-pannel';
    import {
        DEVICE_LIST_SERVICE,
        DEVICE_DELETE_SERVICE,
        DEVICE_DELETE_BATCH_SERVICE,
        DEVICE_BATCH_SERVICE,
        ORG_ALL_LIST_SERVICE
    } from "../../../axios/api";
    import {
        Poptip
    } from 'view-design';

    import busDeviceSave from './busDeviceSave';
    import busDevicePointList from './busDevicePointList';
    import Treeselect from '@riophae/vue-treeselect';
    import '@riophae/vue-treeselect/dist/vue-treeselect.css';
    import TreeImpl from '../../../libs/fw-core/fw-tree/tree';

    export default {
        components: {
            tablePannel,
            busDeviceSave,
            busDevicePointList,
            Treeselect
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
                        orgId: null,
                        keyword: ""
                    },
                    loading: false
                },

                currentComponent: "",
                currentComponentKey: "",

                showOkButton: false,

                orgTreeList: [],
                orgTreeNormalizer(node) {

                    if (node.children && !node.children.length) {
                        delete node.children;
                    }

                    return {
                        id: node.id,
                        label: node.orgName,
                        children: node.children,
                    }
                }

            }
        },
        mounted() {
            this.getOrgList();
            this.onSearch();
        },
        methods: {
            onSearch() {
                this.resetPage();
                this.getDeviceList();
            },
            getDeviceList() {

                var vm = this;

                this.modelTable.loading = true;

                DEVICE_LIST_SERVICE(Object.assign(this.modelTable.filter, {

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
            onAddDevice() {

                this.showOkButton = true;

                this.currentComponent = busDeviceSave;
                this.currentComponentKey = null;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "添加设备";
            },
            onEditDevice(device) {

                this.showOkButton = true;

                this.currentComponent = busDeviceSave;
                this.currentComponentKey = device.id;

                this.$refs.tablePannel.drawerTitle = "编辑设备（" + device.deviceName + "）";
                this.$refs.tablePannel.showDrawer = true;

                this.modelForm.id = device.id;
            },
            onBindDevicePoint(device) {
                this.showOkButton = false;

                this.currentComponent = busDevicePointList;
                this.currentComponentKey = device.id;

                this.$refs.tablePannel.drawerTitle = "设置监测项目（" + device.deviceName + "）";
                this.$refs.tablePannel.showDrawer = true;

                this.modelForm.id = device.deviceCode;
            },
            onOk() {

                this.$refs.tablePannel.showLoading = true;

                switch (this.currentComponent.name) {
                    case "busDeviceSave":
                        this.$refs.drawerComponent.onSaveDevice();
                        break;
                    default:
                        break;
                };

            },
            onCancel() {

                this.currentComponent = null;
                this.currentComponentKey = null;

                this.$refs.tablePannel.showDrawer = false;
                this.$refs.tablePannel.showLoading = false;

                this.modelForm.id = null;

            },
            onSaveSuccess() {

                this.getDeviceList();
                this.onCancel();

            },
            onSaveError() {
                this.$refs.tablePannel.showLoading = false;
            },
            onBatchDevice(device) {

                var statusCode = device.statusCode == 0 ? 1 : 0;

                DEVICE_BATCH_SERVICE({
                    ids: device.id,
                    statusCode: statusCode
                }).then(response => {
                    if (response.success) {
                        this.$Message.success(device.statusCode == 0 ? '停用成功' : '启用成功');
                        this.getDeviceList();
                    }
                });
            },
            onDeleteDevice(device) {
                DEVICE_DELETE_SERVICE({
                    id: device.id,
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getDeviceList();
                    }
                });
            },
            onDeleteBatchDevice() {

                var ids = [];
                var deviceList = this.$refs.modelTable.getCheckboxRecords();
                _.forEach(deviceList, function (device) {
                    ids.push(device.id)
                });

                DEVICE_DELETE_BATCH_SERVICE({
                    ids: _.join([ids], ','),
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getDeviceList();
                    }
                });
            },
            getOrgList() {

                var vm = this;

                ORG_ALL_LIST_SERVICE({}).then(response => {

                    var result = response.result;

                    if (!_.isNil(result)) {

                        var menuTreeImpl = new TreeImpl(result, 'id', 'orgParentId', 'children');
                        vm.orgTreeList = menuTreeImpl.treeList || [];

                    };

                });

            },
            onTableCheckChange() {
                this.modelTable.select = this.$refs.modelTable.getCheckboxRecords();
            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getDeviceList();
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getDeviceList();
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