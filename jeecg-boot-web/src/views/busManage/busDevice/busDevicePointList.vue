<template>
    <table-pannel ref="tablePannel">
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
                <vxe-table-column type="seq" width="60" title="序号" align="center"></vxe-table-column>
                <vxe-table-column field="pointName" width="180" title="监测项目名称"></vxe-table-column>
                <vxe-table-column field="pointStandardU" title="标准值上限"></vxe-table-column>
                <vxe-table-column field="pointStandardL" title="标准值下限"></vxe-table-column>
                <vxe-table-column field="action" title="绑定" align="center">
                    <template v-slot="{ row }">
                        <Switch :value="row.deviceCodes!=null&&row.deviceCodes.indexOf(id)>-1?true:false"
                            @on-change="onBindDevicePoint(row)" />
                    </template>
                </vxe-table-column>
            </vxe-table>
        </slot>

    </table-pannel>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import tablePannel from '../../../components/table-pannel';
    import {
        DEVICE_POINT_ALL_LIST_SERVICE,
        DEVICE_POINT_ADD_SERVICE,
        DEVICE_POINT_DELETE_SERVICE
    } from "../../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import {
        ACCESS_TOKEN
    } from '../../../store/mutations';

    export default {
        name: "busDevicePointList",
        components: {
            tablePannel
        },
        props: {
            id: {
                type: String
            }
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


                ref: ""

            }
        },
        mounted() {
            this.onSearch();
        },
        methods: {
            onSearch() {
                this.resetPage();
                this.getDevicePointList();
            },
            getDevicePointList() {

                this.modelTable.loading = true;

                DEVICE_POINT_ALL_LIST_SERVICE(Object.assign(this.modelTable.filter)).then(response => {

                    var result = response.result;

                    if (!_.isNil(result)) {
                        this.modelTable.data = result || [];
                          this.modelTable.paging.total = result.length;
                    };

                    this.modelTable.loading = false;

                });

            },
            onBindDevicePoint(row) {

                var vm = this;

                if (!_.isNil(row.deviceCodes) && row.deviceCodes.indexOf(this.id) > -1) {
                    DEVICE_POINT_DELETE_SERVICE({
                        deviceCode: this.id,
                        pointCode: row.pointCode
                    }).then(response => {

                        if (response.success) {
                            this.$Message.success('取消设置成功');
                            vm.getDevicePointList();
                        }
                    });
                } else {
                    DEVICE_POINT_ADD_SERVICE({
                        deviceCode: this.id,
                        pointId: row.pointCode
                    }).then(response => {

                        if (response.success) {
                            this.$Message.success('设置成功');
                            vm.getDevicePointList();
                        }
                    });
                }

            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getDevicePointList();
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getDevicePointList();
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