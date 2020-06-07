<template>
    <table-pannel ref="tablePannel" @on-ok="onOk" @on-cancel="onCancel">
        <slot slot="alert">
            <Alert show-icon>菜单管理：支持添加菜单、停用菜单、删除菜单、设置菜单图标等功能</Alert>
        </slot>
        <slot slot="filter">
            <table>
                 <tr>
                    <td>
                        <Select clearable @on-change="onSearch" v-model="modelTable.filter.statusCode"
                            placeholder="请选择菜单状态" style="width:140px">
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
                        <Button type="primary" @click="onAddMenu">添加菜单</Button>
                    </td>
                    <td>
                        <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteBatchMenu">
                            <Button type="error" ghost :disabled="this.modelTable.select<=0">批量删除</Button>
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
                :checkbox-config="{labelField: 'menuName'}" @select-change="onTableCheckChange">
                <vxe-table-column type="checkbox" field="menuName" title="菜单名称" width="260" tree-node>
                </vxe-table-column>
                <vxe-table-column field="menuCode" title="菜单编码" align="center"></vxe-table-column>
                <vxe-table-column field="menuIconProtContent" title="菜单图标" width="120" align="center">
                    <template v-slot="{ row }">
                        <span :class="row.menuIconProtContent"></span>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="statusCode" title="状态" align="center"  width="120">
                    <template v-slot="{ row }">
                        <Tag v-if="row.statusCode=='0'" color="success">正常</Tag>
                        <Tag v-if="row.statusCode=='1'" color="warning">停用</Tag>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="sort" title="菜单顺序" align="right"  width="80"> </vxe-table-column>
                <vxe-table-column field="action" title="操作" align="center">
                    <template v-slot="{ row }">
                        <a href="javascript:void(0)" @click="onEditMenu(row)">
                            编辑
                        </a>
                        <Divider type="vertical" />
                         <a href="javascript:void(0)" @click="onAddChildMenu(row)">
                            添加子菜单
                        </a>
                        <Divider type="vertical" />
                        <Dropdown transfer>
                            <a href="javascript:void(0)">
                                更多
                                <Icon type="ios-arrow-down"></Icon>
                            </a>
                            <DropdownMenu slot="list">
                                <div>
                                    <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteMenu(row)">
                                        <DropdownItem>删除菜单</DropdownItem>
                                    </Poptip>
                                </div>
                                <div>
                                    <Poptip v-if="row.statusCode=='0'" transfer confirm title="确定停用吗？"
                                        @on-ok="onBatchMenu(row)">
                                        <DropdownItem>停用菜单</DropdownItem>
                                    </Poptip>
                                    <Poptip v-if="row.statusCode=='1'" transfer confirm title="确定启用吗？"
                                        @on-ok="onBatchMenu(row)">
                                        <DropdownItem>启用菜单</DropdownItem>
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
                :parentMenuId="modelForm.parentMenuId"></component>
        </slot>
    </table-pannel>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import tablePannel from '../../components/table-pannel';
    import {
        MENU_ALL_LIST_SERVICE,
        MENU_DELETE_SERVICE,
        MENU_DELETE_BATCH_SERVICE,
        MENU_BATCH_SERVICE
    } from "../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import Treeselect from '@riophae/vue-treeselect';
    import '@riophae/vue-treeselect/dist/vue-treeselect.css';
    import menuSave from './sysMenuSave';
    import TreeImpl from "../../libs/tree";

    export default {
        components: {
            tablePannel,
            Treeselect,
            menuSave
        },
        data() {
            return {

                modelForm: {
                    id: "",
                    parentMenuId: ""
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

            this.getMenuList();
        },
        methods: {
            onSearch() {
                this.getMenuList();
            },
            getMenuList() {

                var vm = this;

                this.modelTable.loading = true;

                MENU_ALL_LIST_SERVICE(this.modelTable.filter).then(response => {

                    var result = response.result;

                    if (!_.isNil(result)) {

                        vm.modelTable.paging.total = result.length;
                        var menuTreeImpl = new TreeImpl(result, 'id', 'parentMenuId', 'children');
                        vm.modelTable.data = menuTreeImpl.treeList || [];

                    };

                    vm.modelTable.loading = false;

                });

            },
            onAddMenu() {

                this.currentComponent = menuSave;
                this.currentComponentKey = null;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "添加菜单";

                this.modelForm.id = null;
                this.modelForm.parentMenuId = null;


            },
            onAddChildMenu(menu) {

                this.currentComponent = menuSave;
                this.currentComponentKey = menu.id;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "添加子菜单（" + menu.menuName + "）";

                this.modelForm.parentMenuId = menu.id;

            },
            onEditMenu(menu) {

                this.currentComponent = menuSave;
                this.currentComponentKey = menu.id;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "编辑菜单（" + menu.menuName + "）";

                this.modelForm.id = menu.id;

            },
            onOk() {

                this.$refs.tablePannel.showLoading = true;
                this.$refs.drawerComponent.onSaveMenu();

            },
            onCancel() {

                this.currentComponent = null;
                this.currentComponentKey = null;

                this.$refs.tablePannel.showDrawer = false;
                this.$refs.tablePannel.showLoading = false;

                this.modelForm.id = null;
                this.modelForm.parentMenuId = null;

            },
            onSaveSuccess() {

                this.getMenuList();
                this.onCancel();

            },
            onSaveError() {
                this.$refs.tablePannel.showLoading = false;
            },
            onBatchMenu(menu) {

                var statusCode = menu.statusCode == 0 ? 1 : 0;

                MENU_BATCH_SERVICE({
                    ids: menu.id,
                    statusCode: statusCode
                }).then(response => {
                    if (response.success) {
                        this.$Message.success(menu.statusCode == 0 ? '停用成功' : '启用成功');
                        this.getMenuList();
                    }
                });
            },
            onDeleteMenu(menu) {
                MENU_DELETE_SERVICE({
                    id: menu.id,
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getMenuList();
                    }
                });
            },
            onDeleteBatchMenu() {

                var ids = [];
                var userList = this.$refs.modelTable.getCheckboxRecords();
                _.forEach(userList, function (menu) {
                    ids.push(menu.id)
                });

                MENU_DELETE_BATCH_SERVICE({
                    ids: _.join([ids], ','),
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getMenuList();
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
    @import "../../components/font-icon-select/iconfont/iconfont.css";
</style>