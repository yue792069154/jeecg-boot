<template>
    <table-pannel ref="tablePannel">
        <slot slot="alert">
            <Alert show-icon>角色授权菜单</Alert>
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
        <slot slot="summary">总计：筛选符合条件记录：{{modelTable.data.length}} 条</slot>
        <slot slot="grid">
            <vxe-table ref="modelTable" border stripe resizable highlight-current-row show-header-overflow show-overflow
                highlight-hover-row :loading="modelTable.loading" :data="modelTable.data" row-key row-id="id"
                :tree-config="{children: 'children', accordion: true, line: true, iconOpen: 'vxe-icon--caret-right rotate45', iconClose: 'vxe-icon--caret-right'}"
                :checkbox-config="{labelField: 'menuName'}">
                <vxe-table-column type="checkbox" field="menuName" title="菜单名称" tree-node></vxe-table-column>
                <vxe-table-column field="menuCode" title="菜单编码" align="center"></vxe-table-column>
                <vxe-table-column field="menuIconProtContent" title="菜单图标" width="80" align="center">
                    <template v-slot="{ row }">
                        <span :class="row.menuIconProtContent"></span>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="statusCode" title="状态" align="center" width="120">
                    <template v-slot="{ row }">
                        <Tag v-if="row.statusCode=='0'" color="success">正常</Tag>
                        <Tag v-if="row.statusCode=='1'" color="warning">停用</Tag>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="sort" title="菜单顺序" align="right" width="80"> </vxe-table-column>
            </vxe-table>
        </slot>

    </table-pannel>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import tablePannel from '../../components/table-pannel';
    import {
        MENU_ALL_LIST_SERVICE,
        ROLE_MENU_LIST_SERVICE,
        ROLE_MENU_ADD_SERVICE,
        ROLE_MENU_DELETE_SERVICE
    } from "../../axios/api";
    import TreeImpl from "../../libs/tree";
    export default {
        name: "roleMenuList",
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

                modelTable: {
                    data: [],
                    keys: [],
                    paging: {

                    },
                    filter: {
                        statusCode: "",
                        keyword: ""
                    },
                    loading: false
                }
            }
        },
        mounted() {
            this.getAuthMenuList();
        },
        methods: {
            getAuthMenuList() {

                var vm = this;

                var id = this.id;

                if (!_.isNil(id)) {

                    MENU_ALL_LIST_SERVICE(this.modelTable.filter).then(response => {

                        var result = response.result;

                        if (!_.isNil(result)) {

                            var menuTreeImpl = new TreeImpl(result, 'id', 'parentMenuId', 'children');
                            vm.modelTable.data = menuTreeImpl.treeList || [];

                        };

                        this.modelTable.loading = false;

                    }).then((response => {

                        ROLE_MENU_LIST_SERVICE({
                            roleId: id
                        }).then(response => {

                            var result = response.result;
                            if (!_.isNil(result)) {

                                var checkList = [];
                                _.forEach(result, function (menu) {
                                    checkList.push(vm.$refs.modelTable.getRowById(menu.menuId));
                                });

                                vm.$refs.modelTable.setCheckboxRow(checkList, true);

                            };
                        });


                    }));
                };

            },
            onSaveAuthMenu() {

                var vm = this;

                var ids = [];
                var menuList = this.$refs.modelTable.getCheckboxRecords();
                _.forEach(menuList, function (menu) {
                    ids.push(menu.id)
                });


                ROLE_MENU_ADD_SERVICE({
                    roleId: this.id,
                    menuIds: ids.join(","),
                }).then(response => {
                    if (response.success) {

                        vm.$Message.success('授权成功');
                        vm.$emit("on-save-success");

                    }
                });


            }

        }
    };
</script>
<style lang="less" scoped>

</style>