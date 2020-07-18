<template>
    <div>
        <Timeline v-if="modelTable.data.length>0">
            <TimelineItem v-for="(item,index) in modelTable.data" v-bind:key="index">
                <p class="time" v-text="item.createTime"></p>
                <p class="content">
                    【<label v-text="item.realName"></label>】
                    <label v-text="item.warningHandleDesc"></label>
                </p>
            </TimelineItem>
        </Timeline>
        <div class="no-data" v-if="modelTable.data.length<=0">
            <div>
                <span class="icon iconfont iconnoData"></span>
                <div>无处理信息</div>
            </div>
        </div>
    </div>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import {
        WARNING_HANDLE_ALL_LIST_SERVICE
    } from "../../../axios/api";
    import {
        ACCESS_TOKEN
    } from '../../../store/mutations';
    import {
        ObjectUtils
    } from "../../../libs/fw-core/fw-util";
    import moment from 'moment';

    export default {
        name: "busWarningProcess",
        components: {

        },
        props: {
            id: {
                type: String
            },
        },
        data() {
            return {
                modelTable: {
                    data: []
                }

            }
        },
        mounted() {
            this.getWarningHandle();
        },
        methods: {
            getWarningHandle() {

                var vm = this;

                var id = this.id;

                if (!_.isNil(id)) {

                    WARNING_HANDLE_ALL_LIST_SERVICE({
                        warningId: id
                    }).then(response => {

                        var result = response.result;
                        if (ObjectUtils.hasValue(result)) {
                            vm.modelTable.data = result || [];
                        };
                        
                    });

                };
            }
        }
    };
</script>
<style lang="less" scoped>
    @import "../../../components/font-icon-select/iconfont/iconfont.css";

    .time {
        font-size: 14px;
        font-weight: bold;
    }

    .content {
        padding-left: 5px;
    }

    .no-data {
        display: flex;
        text-align: center;
        justify-content: center;
        align-items: center;

        span {
            font-size: 50px;
        }
    }
</style>