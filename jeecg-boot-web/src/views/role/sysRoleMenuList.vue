<template>
    <table-pannel ref="tablePannel">
        <slot slot="alert">
            <Alert show-icon>角色授权菜单</Alert>
        </slot>
        <slot slot="filter">
            <table>
                <tr>
                    <td>
                        <Input suffix="ios-search" @on-change="getAuthMenuList" v-model="modelTable.filter.keyword"
                            placeholder="请输入关键字查询" style="width: auto" />
                    </td>
                </tr>
            </table>
        </slot>
        <slot slot="summary">总计：筛选符合条件记录：{{modelTable.data.length}} 条</slot>
        <slot slot="grid">
            <vxe-table ref="modelTable" border stripe resizable highlight-current-row show-header-overflow show-overflow
                highlight-hover-row :loading="modelTable.loading" :data="modelTable.data" row-key row-id="key"
                :tree-config="{children: 'children', accordion: true, line: true, iconOpen: 'vxe-icon--caret-right rotate45', iconClose: 'vxe-icon--caret-right'}"
                :checkbox-config="{labelField: 'menuName'}">
                <vxe-table-column type="checkbox" field="menuName" title="菜单名称" tree-node></vxe-table-column>
                <vxe-table-column field="menuCode" title="菜单编码" align="center"></vxe-table-column>
                <vxe-table-column field="menuIconProtContent" title="菜单图标" width="80" align="center">
                    <template v-slot="{ row }">
                        <span :class="row.menuIconProtContent"></span>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="sort" title="菜单顺序" align="right"></vxe-table-column>
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
        ROLE_AUTH_MENU_LIST_SERVICE,
        ROLE_AUTH_MENU_ADD_SERVICE
    } from "../../axios/api";
     import TreeImpl from "../../libs/tree";
    export default {
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
                        menuName: ""
                    },
                    loading: false
                },
                cacheCheckList: []
            }
        },
        watch: {
            id(newValue, oldValue) {
                this.getAuthMenuList();
            }
        },
        mounted() {

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

                        ROLE_AUTH_MENU_LIST_SERVICE({
                            roleId: id
                        }).then(response => {

                            var result = response.result;
                            if (!_.isNil(result)) {


                                vm.cacheCheckList = result;

                                var checkList = [];
                                _.forEach(result, function (id) {
                                    checkList.push(vm.$refs.modelTable.getRowById(id));
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
                    ids.push(menu.key)
                });


                ROLE_AUTH_MENU_ADD_SERVICE({
                    roleId: this.id,
                    permissionIds: ids.join(",") || [],
                    lastpermissionIds: this.cacheCheckList.join(",") || [],
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