<template>
    <table-pannel ref="tablePannel" @on-ok="onOk" @on-cancel="onCancel" :show-ok-button="showOkButton">
        <slot slot="alert">
            <Alert show-icon>监测项目管理：监测项目统一管理，支持监测项目的增删改查、监测项目监测项目动态维护、监测项目所属机构维护</Alert>
        </slot>
        <slot slot="filter">
            <table>
                <tr>
                    <td>
                        <Input suffix="ios-search" @on-change="onSearch" v-model="modelTable.filter.keyword"
                            placeholder="请输入关键字查询" style="width: auto" />
                    </td>
                </tr>
            </table>
        </slot>
        <slot slot="action">

            <table>
                <tr>
                    <td>
                        <Button type="primary" @click="onAddPoint">添加监测项目</Button>
                    </td>
                    <td>
                        <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteBatchPoint">
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
                <vxe-table-column field="pointName" title="监测项目名称"></vxe-table-column>
                <vxe-table-column field="pointCode" title="监测项目编码"></vxe-table-column>
                <vxe-table-column field="pointUnit" align="center" title="监测项目计量单位"></vxe-table-column>
                <vxe-table-column field="pointStandardU" align="right" title="监测项目标准值上限"></vxe-table-column>
                <vxe-table-column field="pointStandardL" align="right" title="监测项目标准值下限"></vxe-table-column>
                 <vxe-table-column field="sort" title="监测项目顺序" align="right" width="160"> </vxe-table-column>
                <vxe-table-column field="action" width="150" title="操作" align="center">
                    <template v-slot="{ row }">
                        <a href="javascript:void(0)" @click="onEditPoint(row)">
                            编辑
                        </a>
                        <div class="split"></div>
                        <Dropdown transfer>
                            <a href="javascript:void(0)">
                                更多
                                <Icon type="ios-arrow-down"></Icon>
                            </a>
                            <DropdownMenu slot="list">
                                <div>
                                    <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeletePoint(row)">
                                        <DropdownItem>删除监测项目</DropdownItem>
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
        POINT_LIST_SERVICE,
        POINT_DELETE_SERVICE,
        POINT_DELETE_BATCH_SERVICE,
        POINT_BATCH_SERVICE
    } from "../../../axios/api";
    import {
        Poptip
    } from 'view-design';

    import busPointSave from './busPointSave';

    export default {
        components: {
            tablePannel,
            busPointSave
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
                        keyword: ""
                    },
                    loading: false
                },

                currentComponent: "",
                currentComponentKey: "",

                showOkButton: false

            }
        },
        mounted() {
            this.onSearch();
        },
        methods: {
            onSearch() {
                this.resetPage();
                this.getPointList();
            },
            getPointList() {

                var vm = this;

                this.modelTable.loading = true;

                POINT_LIST_SERVICE(Object.assign(this.modelTable.filter, {

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
            onAddPoint() {

                this.showOkButton = true;

                this.currentComponent = busPointSave;
                this.currentComponentKey = null;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "添加监测项目";
            },
            onEditPoint(point) {

                this.showOkButton = true;

                this.currentComponent = busPointSave;
                this.currentComponentKey = point.id;

                this.$refs.tablePannel.drawerTitle = "编辑监测项目（" + point.pointName + "）";
                this.$refs.tablePannel.showDrawer = true;

                this.modelForm.id = point.id;
            },
            onOk() {

                this.$refs.tablePannel.showLoading = true;

                switch (this.currentComponent.name) {
                    case "busPointSave":
                        this.$refs.drawerComponent.onSavePoint();
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

                this.getPointList();
                this.onCancel();

            },
            onSaveError() {
                this.$refs.tablePannel.showLoading = false;
            },
            onDeletePoint(point) {
                POINT_DELETE_SERVICE({
                    id: point.id,
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getPointList();
                    }
                });
            },
            onDeleteBatchPoint() {

                var ids = [];
                var pointList = this.$refs.modelTable.getCheckboxRecords();
                _.forEach(pointList, function (point) {
                    ids.push(point.id)
                });

                POINT_DELETE_BATCH_SERVICE({
                    ids: _.join([ids], ','),
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getPointList();
                    }
                });
            },
            onTableCheckChange() {
                this.modelTable.select = this.$refs.modelTable.getCheckboxRecords();
            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getPointList();
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getPointList();
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