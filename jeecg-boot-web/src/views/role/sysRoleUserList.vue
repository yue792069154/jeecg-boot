<template>
    <table-pannel ref="tablePannel">
        <slot slot="filter">
            <table>
                <tr>
                    <td>
                        <Select clearable @on-change="onSearch" v-model="modelTable.filter.statusCode"
                            placeholder="请选择用户状态" style="width:140px">
                            <Option value="0">正常</Option>
                            <Option value="1">冻结</Option>
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
        <slot slot="summary">总计：筛选符合条件记录：{{modelTable.paging.total}} 条</slot>
        <slot slot="grid">
            <vxe-table ref="modelTable" border stripe resizable highlight-current-row show-header-overflow show-overflow
                highlight-hover-row :loading="modelTable.loading" :data="modelTable.data">
                <vxe-table-column type="seq" width="60" title="序号" align="center"></vxe-table-column>
                <vxe-table-column field="userName" title="用户账号"></vxe-table-column>
                <vxe-table-column field="realName" title="真实姓名"></vxe-table-column>
                <vxe-table-column field="avatar" title="用户头像" align="center">
                    <template v-slot="{ row }">
                        <Avatar v-if="row.avatar!=null&&row.avatar!=''" :src="fileViewServiceUrl+row.avatar" />
                        <Avatar v-else icon="ios-person" />
                    </template>
                </vxe-table-column>
                <vxe-table-column field="statusCode" width="80" title="状态" align="center">
                    <template v-slot="{ row }">
                        <Tag v-if="row.statusCode=='0'" color="success">正常</Tag>
                        <Tag v-if="row.statusCode=='1'" color="warning">停用</Tag>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="action" title="授权" align="center">
                    <template v-slot="{ row }">
                        <Switch :value="row.sysRoleIds!=null&&row.sysRoleIds.indexOf(id)>-1?true:false"
                            @on-change="onAuthUser(row)" />
                    </template>
                </vxe-table-column>
            </vxe-table>
        </slot>

    </table-pannel>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import tablePannel from '../../components/table-pannel';
    import {
        USER_ALL_LIST_SERVICE,
        ROLE_ADD_USER_SERVICE,
        ROLE_DELETE_USER_SERVICE,
        FILE_VIEW_SERVICE_URL
    } from "../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import {
        ACCESS_TOKEN
    } from '../../store/mutations';

    export default {
        name: "roleUserList",
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
                    paging: {
                        pageSize: 10,
                        pageIndex: 1,
                        total: 0
                    },
                    filter: {
                        statusCode: "",
                        keyword: ""
                    },
                    loading: false
                },

                fileViewServiceUrl: FILE_VIEW_SERVICE_URL,

                ref: ""

            }
        },
        mounted() {
            this.onSearch();
        },
        methods: {
            onSearch() {
                this.resetPage();
                this.getUserList();
            },
            getUserList() {

                this.modelTable.loading = true;

                USER_ALL_LIST_SERVICE(Object.assign(this.modelTable.filter)).then(response => {

                    var result = response.result;

                    if (!_.isNil(result)) {
                        this.modelTable.data = result || []
                    };

                    this.modelTable.loading = false;

                });

            },
            onAuthUser(row) {

                var vm = this;

                if (!_.isNil(row.sysRoleIds) && row.sysRoleIds.indexOf(this.id) > -1) {
                    ROLE_DELETE_USER_SERVICE({
                        roleId: this.id,
                        userId: row.id
                    }).then(response => {

                        if (response.success) {
                            this.$Message.success('取消授权成功');
                            vm.getUserList();
                        }
                    });
                } else {
                    ROLE_ADD_USER_SERVICE({
                        roleId: this.id,
                        userId: row.id
                    }).then(response => {

                        if (response.success) {
                            this.$Message.success('授权成功');
                            vm.getUserList();
                        }
                    });
                }

            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getUserList();
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getUserList();
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

</style>