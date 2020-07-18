<template>
    <table-pannel ref="tablePannel" @on-ok="onOk" @on-cancel="onCancel">
        <slot slot="alert">
            <Alert show-icon>机构管理：支持添加机构、停用机构、删除机构等功能</Alert>
        </slot>
        <slot slot="filter">
            <table>
                <tr>
                    <td>
                        <Select clearable @on-change="onSearch" v-model="modelTable.filter.statusCode"
                            placeholder="请选择机构状态" style="width:140px">
                            <Option value="0">正常</Option>
                            <Option value="1">停用</Option>
                        </Select>
                    </td>
                    <td>
                        <Divider type="vertical" />
                    </td>
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
                        <Button type="primary" @click="onAddMenu" >添加机构</Button>
                    </td>
                    <td>
                        <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteBatchOrg">
                            <Button type="error" ghost
                                :disabled="this.modelTable.select<=0">批量删除</Button>
                        </Poptip>
                    </td>

                </tr>
            </table>
        </slot>
        <slot slot="summary">总计：筛选符合条件记录：{{modelTable.paging.total}} 条</slot>
        <slot slot="grid">
            <vxe-table ref="modelTable" border stripe resizable highlight-current-row show-header-overflow show-overflow
                highlight-hover-row :loading="modelTable.loading" :data="modelTable.data" row-key row-id="id"
                :tree-config="{children: 'children', accordion: true, line: true, iconOpen: 'vxe-icon--caret-right rotate45', iconClose: 'vxe-icon--caret-right'}"
                :checkbox-config="{labelField: 'orgName'}" @select-change="onTableCheckChange">
                <vxe-table-column type="checkbox" field="orgName" title="机构名称"  tree-node>
                </vxe-table-column>
                <vxe-table-column field="statusCode" title="状态" align="center" width="120">
                    <template v-slot="{ row }">
                        <Tag v-if="row.statusCode=='0'" color="success">正常</Tag>
                        <Tag v-if="row.statusCode=='1'" color="warning">停用</Tag>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="sort" title="机构顺序" align="right" width="100"> </vxe-table-column>
                <vxe-table-column field="action" title="操作" align="center" width="280">
                    <template v-slot="{ row }">
                        <a href="javascript:void(0)" @click="onEditOrg(row)">
                            编辑
                        </a>
                       <div class="split"></div>
                        <a href="javascript:void(0)" @click="onAddChildOrg(row)">
                            添加子机构
                        </a>
                         <div class="split"></div>
                        <Dropdown transfer >
                            <a href="javascript:void(0)">
                                更多
                                <Icon type="ios-arrow-down"></Icon>
                            </a>
                            <DropdownMenu slot="list">

                                <div >
                                    <Poptip v-if="row.statusCode=='0'" transfer confirm title="确定停用吗？"
                                        @on-ok="onBatchOrg(row)">
                                        <DropdownItem>停用机构</DropdownItem>
                                    </Poptip>
                                    <Poptip v-if="row.statusCode=='1'" transfer confirm title="确定启用吗？"
                                        @on-ok="onBatchOrg(row)">
                                        <DropdownItem>启用机构</DropdownItem>
                                    </Poptip>
                                </div>
                                <div >
                                    <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteOrg(row)">
                                        <DropdownItem>删除机构</DropdownItem>
                                    </Poptip>
                                </div>
                            </DropdownMenu>
                        </Dropdown>
                    </template>
                </vxe-table-column>
            </vxe-table>
        </slot>
        <slot slot="page">

        </slot>
        <slot slot="drawer">
            <component ref="drawerComponent" :key="currentComponentKey" :is="currentComponent"
                @on-save-error="onSaveError" @on-save-success="onSaveSuccess" :id="modelForm.id"
                :orgParentId="modelForm.orgParentId"></component>
        </slot>
    </table-pannel>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import tablePannel from '../../../components/table-pannel';
    import {
        ORG_ALL_LIST_SERVICE,
        ORG_DELETE_SERVICE,
        ORG_DELETE_BATCH_SERVICE,
        ORG_BATCH_SERVICE
    } from "../../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import Treeselect from '@riophae/vue-treeselect';
    import '@riophae/vue-treeselect/dist/vue-treeselect.css';
    import busOrgSave from './busOrgSave';
    import TreeImpl from '../../../libs/fw-core/fw-tree/tree';

    export default {
        components: {
            tablePannel,
            Treeselect,
            busOrgSave
        },
        data() {
            return {

                modelForm: {
                    id: "",
                    orgParentId: ""
                },
                modelTable: {
                    data: [],
                    paging: {

                    },
                    select: [],
                    filter: {
                        statusCode: "",
                        keyword: ""
                    },
                    loading: false
                },

                currentComponent: "",
                currentComponentKey: ""
            }
        },
        mounted() {

            this.getOrgList();
        },
        methods: {
            onSearch() {
                this.getOrgList();
            },
            getOrgList() {

                var vm = this;

                this.modelTable.loading = true;

                ORG_ALL_LIST_SERVICE(this.modelTable.filter).then(response => {

                    var result = response.result;

                    if (!_.isNil(result)) {

                        vm.modelTable.paging.total = result.length;
                        var menuTreeImpl = new TreeImpl(result, 'id', 'orgParentId', 'children');
                        vm.modelTable.data = menuTreeImpl.treeList || [];

                    };

                    vm.modelTable.loading = false;

                });

            },
            onAddMenu() {


                this.currentComponent = busOrgSave;
                this.currentComponentKey = null;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "添加机构";

                this.modelForm.id = null;
                this.modelForm.orgParentId = null;


            },
            onAddChildOrg(org) {

                this.currentComponent = busOrgSave;
                this.currentComponentKey = org.id;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "添加子机构（" + org.orgName + "）";

                this.modelForm.orgParentId = org.id;

            },
            onEditOrg(org) {

                this.currentComponent = busOrgSave;
                this.currentComponentKey = org.id;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "编辑机构（" + org.orgName + "）";

                this.modelForm.id = org.id;

            },
            onOk() {

                this.$refs.tablePannel.showLoading = true;
                this.$refs.drawerComponent.onSaveOrg();

            },
            onCancel() {

                this.currentComponent = null;
                this.currentComponentKey = null;

                this.$refs.tablePannel.showDrawer = false;
                this.$refs.tablePannel.showLoading = false;

                this.modelForm.id = null;
                this.modelForm.orgParentId = null;

            },
            onSaveSuccess() {

                this.getOrgList();
                this.onCancel();

            },
            onSaveError() {
                this.$refs.tablePannel.showLoading = false;
            },
            onBatchOrg(org) {

                var statusCode = org.statusCode == 0 ? 1 : 0;

                ORG_BATCH_SERVICE({
                    ids: org.id,
                    statusCode: statusCode
                }).then(response => {
                    if (response.success) {
                        this.$Message.success(org.statusCode == 0 ? '停用成功' : '启用成功');
                        this.getOrgList();
                    }
                });
            },
            onDeleteOrg(org) {
                ORG_DELETE_SERVICE({
                    id: org.id,
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getOrgList();
                    }
                });
            },
            onDeleteBatchOrg() {

                var ids = [];
                var userList = this.$refs.modelTable.getCheckboxRecords();
                _.forEach(userList, function (org) {
                    ids.push(org.id)
                });

                ORG_DELETE_BATCH_SERVICE({
                    ids: _.join([ids], ','),
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getOrgList();
                    }
                });
            },
            onTableCheckChange() {
                this.modelTable.select = this.$refs.modelTable.getCheckboxRecords();
            }

        }
    };
</script>
<style lang="less" scoped>

</style>