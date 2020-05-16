<template>
    <table-pannel ref="tablePannel">
        <slot slot="filter">
            <table>
                <tr>
                    <td>
                        <Select clearable @on-change="onSearch" v-model="modelTable.filter.status" placeholder="请选择用户状态"
                            style="width:140px">
                            <Option value="1">正常</Option>
                            <Option value="2">冻结</Option>
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
            <Table :loading="modelTable.loading" stripe :columns="modelTable.columns" :data="modelTable.data"
                @on-selection-change="getSelectedEntityList">

                <template slot-scope="{ row }" slot="status_dictText">
                    <Tag v-if="row.status==1" closable color="success" v-text="row.status_dictText"></Tag>
                    <Tag v-if="row.status==2" closable color="warning" v-text="row.status_dictText"></Tag>
                </template>

                <template slot-scope="{ row }" slot="avatar">
                    <Avatar v-if="row.avatar!=null&&row.avatar!=''" :src="fileViewServiceUrl+row.avatar" />
                    <Avatar v-else icon="ios-person" />
                </template>
            </Table>
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
    import _ from 'lodash';
    import tablePannel from '../../components/table-pannel';
    import {
        USER_LIST_SERVICE,
        USER_ROLE_LIST_SERVICE,
        FILE_VIEW_SERVICE_URL,
        USER_EXPORT_SERVICE_URL,
        USER_IMPORT_SERVICE_URL
    } from "../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import {
        ACCESS_TOKEN
    } from '../../store/mutations';

    export default {
        components: {
            tablePannel
        },
        data() {
            return {
                modelTable: {
                    columns: [{
                            type: 'selection',
                            width: 60,
                            align: 'center'
                        }, {
                            title: '用户账号',
                            key: 'username',
                            ellipsis: true,
                            tooltip: true
                        },

                        {
                            title: '真实姓名',
                            key: 'realname',
                            ellipsis: true,
                            tooltip: true
                        },
                        {
                            title: '用户头像',
                            key: 'avatar',
                            align: "center",
                            ellipsis: true,
                            tooltip: true,
                            slot: 'avatar'
                        },
                        {
                            title: '状态',
                            key: 'status_dictText',
                            align: "center",
                            ellipsis: true,
                            tooltip: true,
                            slot: 'status_dictText'
                        }
                    ],
                    data: [],
                    paging: {
                        pageSize: 10,
                        pageIndex: 1,
                        total: 0
                    },
                    filter: {
                        sex: "",
                        status: "",
                        keyword: "",
                        keywordRange: "username,realname,phone,work_no,email,telephone"
                    },
                    loading: false
                },

                selectedEntityList: [],

                fileViewServiceUrl: FILE_VIEW_SERVICE_URL,

                ref: ""

            }
        },
        mounted() {
            this.getUserList();
        },
        methods: {
            onSearch() {
                this.resetPage();
                this.getUserList();
            },
            getUserList() {

                this.modelTable.loading = true;

                USER_LIST_SERVICE(Object.assign(this.modelTable.filter, {

                    pageNo: this.modelTable.paging.pageIndex,
                    pageSize: this.modelTable.paging.pageSize

                })).then(response => {

                    var result = response.result;

                    if (!_.isNil(result)) {
                        this.modelTable.data = result.records || [];
                        this.modelTable.paging.pageIndex = result.current;
                        this.modelTable.paging.pageSize = result.size;
                        this.modelTable.paging.total = result.total;
                    };

                    this.modelTable.loading = false;

                });

            },
            getSelectedEntityList(selection, row) {
                this.selectedEntityList = selection;
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
                this.modelTable.paging.pageIndex = 1;
                this.modelTable.paging.pageSize = 10;
            }

        }
    };
</script>
<style lang="less" scoped>
    
</style>