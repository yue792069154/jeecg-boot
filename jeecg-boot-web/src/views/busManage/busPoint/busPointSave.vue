<template>
    <Form label-position="top" ref="modelForm" :model="modelForm" :rules="modelFormRule">
        <FormItem label="监测项目名称" prop="pointName">
            <Input placeholder="请输入监测项目名称" v-model="modelForm.pointName" clearable />
        </FormItem>
        <FormItem label="监测项目编码" prop="pointCode">
            <Input placeholder="请输入监测项目编码" v-model="modelForm.pointCode" clearable />
        </FormItem>
        <FormItem label="监测项目计量单位" prop="pointUnit">
            <Select placeholder="请选择监测项目计量单位" v-model="modelForm.pointUnit">
                <Option v-for="item in dict.electricUnit" :value="item.dictCode" :key="item.dictCode"
                    :label="item.dictName+' ( '+item.dictCode+' ) '">
                </Option>
            </Select>
        </FormItem>
        <FormItem label="监测项目标准值上限" prop="pointStandardU">
            <InputNumber placeholder="请输入监测项目标准值上限" v-model="modelForm.pointStandardU" clearable style="width: 100%">
            </InputNumber>
        </FormItem>
        <FormItem label="监测项目标准值下限" prop="pointStandardL">
            <InputNumber placeholder="请输入监测项目标准值下限" v-model="modelForm.pointStandardL" clearable style="width: 100%">
            </InputNumber>
        </FormItem>
        <FormItem label="监测项目顺序" prop="sort">
            <InputNumber placeholder="请输入监测项目顺序" :max="9999" :min="0" v-model="modelForm.sort" style="width:100%">
            </InputNumber>
        </FormItem>
    </Form>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import {
        POINT_QUERY_SERVICE,
        POINT_ADD_SERVICE,
        POINT_EDIT_SERVICE,
        DICT_LIST_BY_DICT_TYPE_CODE_SERVICE
    } from "../../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import {
        ACCESS_TOKEN
    } from '../../../store/mutations';
    import {
        ObjectUtils
    } from "../../../libs/fw-core/fw-util";
    import moment from 'moment';

    export default {
        name: "busPointSave",
        components: {

        },
        props: {
            id: {
                type: String
            },
        },
        data() {
            return {

                modelForm: {
                    id: null,
                    pointName: null,
                    pointCode: null,
                    pointUnit: null,
                    pointStandardU: null,
                    pointStandardL: null,
                    sort:null
                },
                modelFormRule: {
                    pointName: [{
                        required: true,
                        message: '请输入监测项目名称',
                        trigger: 'change,blur'
                    }],
                    pointCode: [{
                        required: true,
                        message: '请输入监测项目编码',
                        trigger: 'change,blur'
                    }],
                    pointUnit: [{
                        required: false,
                        message: '请选择监测项目计量单位',
                        trigger: 'change,blur'
                    }]
                },
                dict: {
                    electricUnit: []
                }

            }
        },
        mounted() {
            this.getDict();
            this.getPoint();
        },
        methods: {
            getPoint() {

                var vm = this;

                var id = this.id;

                if (!_.isNil(id)) {
                    POINT_QUERY_SERVICE({
                        id: id
                    }).then(response => {

                        if (response.success) {
                            var result = response.result;
                            if (!_.isNil(result)) {
                                for (var item in this.modelForm) {
                                    this.modelForm[item] = result[item];
                                };
                            };
                        }
                    });
                };
            },
            getDict() {

                var vm = this;

                DICT_LIST_BY_DICT_TYPE_CODE_SERVICE({
                    dictTypeCode: "electricUnit"
                }).then(response => {
                    if (response.success) {
                        vm.dict.electricUnit = response.result;
                    };
                });

            },
            onSavePoint() {

                var vm = this;

                this.$refs['modelForm'].validate((valid) => {
                    if (valid) {

                        if (!ObjectUtils.hasValue(vm.modelForm.pointStandardU)) {
                            vm.modelForm.pointStandardU = "";
                        }

                        if (!ObjectUtils.hasValue(vm.modelForm.pointStandardL)) {
                            vm.modelForm.pointStandardL = "";
                        }

                        if (_.isNil(vm.modelForm.id)) {

                            POINT_ADD_SERVICE(vm.modelForm).then(response => {
                                if (response.success) {

                                    vm.$Message.success('添加成功');
                                    vm.$emit("on-save-success");
                                    vm.resetFields();

                                }
                            });

                        } else {

                            POINT_EDIT_SERVICE(vm.modelForm).then(response => {
                                if (response.success) {

                                    vm.$Message.success('编辑成功');
                                    vm.$emit("on-save-success");
                                    vm.resetFields();

                                }
                            });
                        }
                    } else {
                        vm.$emit("on-save-error");
                    };
                });


            },
            resetFields() {
                this.$refs.modelForm.resetFields();
                this.modelForm.id = null;
            }
        }
    };
</script>
<style lang="less" scoped>

</style>