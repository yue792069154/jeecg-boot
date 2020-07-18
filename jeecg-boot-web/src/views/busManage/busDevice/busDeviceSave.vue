<template>
    <Form label-position="top" ref="modelForm" :model="modelForm" :rules="modelFormRule">
        <FormItem label="设备所属机构" prop="orgId">
            <treeselect :disable-branch-nodes="true" :show-count="true" :multiple="false" :options="orgTreeList"
                :normalizer="orgTreeNormalizer" placeholder="请选择设备所属机构" v-model="modelForm.orgId" />
        </FormItem>
        <FormItem label="设备名称" prop="deviceName">
            <Input placeholder="请输入设备名称" v-model="modelForm.deviceName" clearable />
        </FormItem>
        <FormItem label="设备出厂编号" prop="deviceCode">
            <Input placeholder="请输入设备出厂编号" v-model="modelForm.deviceCode" clearable />
        </FormItem>
        <FormItem label="设备制造厂" prop="deviceManufacturer">
            <Input placeholder="请输入设备制造厂" v-model="modelForm.deviceManufacturer" clearable />
        </FormItem>
        <FormItem label="设备规格型号" prop="deviceModel">
            <Input placeholder="请输入规设备格型号" v-model="modelForm.deviceModel" clearable />
        </FormItem>
        <FormItem label="设备电源" prop="devicePower">
            <Input placeholder="请输入设备电源" v-model="modelForm.devicePower" clearable />
        </FormItem>
        <FormItem label="设备功率" prop="deviceRate">
            <Input placeholder="请输入设备功率" v-model="modelForm.deviceRate" clearable />
        </FormItem>
        <FormItem label="设备出厂日期" prop="deviceFactoryDate">
            <DatePicker type="date" format="yyyy-MM-dd" placeholder="请选择设备出厂日期" clearable
                v-model="modelForm.deviceFactoryDate" style="width: 100%">
            </DatePicker>
        </FormItem>
        <FormItem label="设备安装线路" prop="设备安装线路">
            <Input placeholder="请输入安装线路" v-model="modelForm.deviceLine" clearable />
        </FormItem>
        <FormItem label="设备安装用户" prop="deviceInstall">
            <Input placeholder="请输入设备安装用户" v-model="modelForm.deviceInstall" clearable />
        </FormItem>
        <FormItem label="设备安装日期" prop="deviceInstallDate">
            <DatePicker type="date" placeholder="请输入设备安装日期" clearable v-model="modelForm.deviceInstallDate"
                style="width: 100%">
            </DatePicker>
        </FormItem>
        <FormItem label="设备安装经度" prop="longitude">
            <InputNumber :max="180" :min="0" placeholder="请输入设备安装经度" v-model="modelForm.longitude" clearable
                style="width: 100%"></InputNumber>
        </FormItem>
        <FormItem label="设备安装纬度" prop="latitude">
            <InputNumber :max="90" :min="0" placeholder="请输入设备安装纬度" v-model="modelForm.latitude" clearable
                style="width: 100%"></InputNumber>
        </FormItem>
    </Form>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import {
        DEVICE_QUERY_SERVICE,
        DEVICE_ADD_SERVICE,
        DEVICE_EDIT_SERVICE,
        DICT_LIST_BY_DICT_TYPE_CODE_SERVICE,
        ORG_ALL_LIST_SERVICE
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
    import Treeselect from '@riophae/vue-treeselect';
    import '@riophae/vue-treeselect/dist/vue-treeselect.css';
    import TreeImpl from '../../../libs/fw-core/fw-tree/tree';

    export default {
        name: "busDeviceSave",
        components: {
            Treeselect
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
                    orgId: null,
                    deviceName: null,
                    deviceManufacturer: null,
                    deviceModel: null,
                    devicePower: null,
                    deviceRate: null,
                    deviceCode: null,
                    deviceFactoryDate: null,
                    deviceType: null,
                    deviceLine: null,
                    deviceInstall: null,
                    deviceInstallDate: null,
                    longitude: null,
                    latitude: null
                },
                modelFormRule: {
                    deviceName: [{
                        required: true,
                        message: '请输入设备名称',
                        trigger: 'change,blur'
                    }],
                    deviceCode: [{
                        required: true,
                        message: '请输入设备出厂编号',
                        trigger: 'change,blur'
                    }],
                    deviceFactoryDate: [{
                        trigger: 'blur',
                        pattern: /.+/
                    }]
                },

                orgTreeList: [],
                orgTreeNormalizer(node) {
                    
                    if (node.children && !node.children.length) {
                        delete node.children;
                    }

                    return {
                        id: node.id,
                        label: node.orgName,
                        children: node.children,
                    }
                }

            }
        },
        mounted() {
            this.getOrgList();
            this.getDevice();
        },
        methods: {
            getDevice() {

                var vm = this;

                var id = this.id;

                if (!_.isNil(id)) {
                    DEVICE_QUERY_SERVICE({
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
            onSaveDevice() {

                var vm = this;

                this.$refs['modelForm'].validate((valid) => {
                    if (valid) {

                        if (ObjectUtils.hasValue(this.modelForm.deviceFactoryDate)) {
                            vm.modelForm.deviceFactoryDate = moment(vm.modelForm.deviceFactoryDate)
                                .format("yyyy-MM-DD HH:mm:ss");
                        }

                        if (ObjectUtils.hasValue(this.modelForm.deviceInstallDate)) {
                            vm.modelForm.deviceInstallDate = moment(vm.modelForm.deviceInstallDate)
                                .format("yyyy-MM-DD HH:mm:ss");
                        }

                        if (_.isNil(vm.modelForm.id)) {



                            DEVICE_ADD_SERVICE(vm.modelForm).then(response => {
                                if (response.success) {

                                    vm.$Message.success('添加成功');
                                    vm.$emit("on-save-success");
                                    vm.resetFields();

                                }
                            });

                        } else {

                            DEVICE_EDIT_SERVICE(vm.modelForm).then(response => {
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
            getOrgList() {

                var vm = this;

                ORG_ALL_LIST_SERVICE({}).then(response => {

                    var result = response.result;

                    if (!_.isNil(result)) {

                        var menuTreeImpl = new TreeImpl(result, 'id', 'orgParentId', 'children');
                        vm.orgTreeList = menuTreeImpl.treeList || [];

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