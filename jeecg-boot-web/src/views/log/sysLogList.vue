<template>
    <table-pannel ref="tablePannel">
        <slot slot="alert">
            <Alert show-icon>系统日志：用户登陆日志、页面请求访问日志</Alert>
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
        <slot slot="summary">总计：筛选符合条件记录：{{modelTable.paging.total}} 条</slot>
        <slot slot="grid">
            <vxe-table ref="modelTable" border stripe resizable highlight-current-row show-header-overflow show-overflow
                highlight-hover-row :loading="modelTable.loading" :data="modelTable.data">
                <vxe-table-column field="logContent" title="日志内容"></vxe-table-column>
                <vxe-table-column field="userId" align="center" title="操作用户账号"></vxe-table-column>
                <vxe-table-column field="userName"  align="center" title="操作用户名称"></vxe-table-column>
                <vxe-table-column field="method" title="请求方法"></vxe-table-column>
                <vxe-table-column field="requestParam" title="请求参数"></vxe-table-column>
                <vxe-table-column field="costTime"  align="right" title="请求耗时（毫秒）"></vxe-table-column>
                <vxe-table-column field="createTime"  align="center" title="请求时间"></vxe-table-column>
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
    import tablePannel from '../../components/table-pannel';
    import {
        LOG_LIST_SERVICE
    } from "../../axios/api";
    import {
        Poptip
    } from 'view-design';
    export default {
        components: {
            tablePannel
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
                        keyword: ""
                    },
                    loading: false
                }

            }
        },
        mounted() {
            this.onSearch();
        },
        methods: {
            onSearch() {
                this.resetPage();
                this.getLogList();
            },
            getLogList() {

                var vm = this;

                this.modelTable.loading = true;

                LOG_LIST_SERVICE(Object.assign(this.modelTable.filter, {

                    pageNo: vm.modelTable.paging.pageIndex,
                    pageSize: vm.modelTable.paging.pageSize

                })).then(response => {

                    var result = response.result;

                    if (!_.isNil(result)) {
                        vm.modelTable.data = result.records || [];
                        vm.modelTable.paging.total = result.total;
                    };

                    vm.modelTable.loading = false;

                });

            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getLogList();
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getLogList();
            },
            resetPage: function () {
                this.modelTable.paging.pageIndex = 1;
                this.modelTable.paging.pageSize = 10;
            }

        }
    };
</script>
<style lang="less" scoped>
    @import "../../style/common.less";

    table {
        td {
            padding: 0px 5px;
        }
    }
</style>