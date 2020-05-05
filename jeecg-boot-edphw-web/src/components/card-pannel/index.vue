<template>
    <div :class="showHeader ? 'card-pannel' : 'card-pannel-padding0' ">
        <div class="card-header" v-if="showHeader">

            <div class="ivu-breadcrumb" v-if="breadcrumb">
                <span v-for="(item,index) in breadcrumbList" @click="onBreadCrumbSelect(item,index)" v-bind:key="index">
                    <span class="ivu-breadcrumb-item-link" v-text="item.title"></span>
                    <span class="ivu-breadcrumb-item-separator">/</span>
                </span>
            </div>

            <div v-else class="card-title" v-text="title">
            </div>

        </div>
        <div class="card-content">
            <slot></slot>
        </div>
    </div>
</template>
<script>
    import _ from 'lodash';
    export default {
        name: 'cardPannel',
        props: {
            title: {
                type: String,
                require: true
            },
            breadcrumb: {
                type: Boolean,
                require: false
            },
            breadcrumbList: {
                type: Array,
                default () {
                    return []
                }
            }
        },
        data() {
            return {
                showHeader: true
            }
        },
        mounted() {
            this.onSetHeader();
        },
        methods: {
            onBreadCrumbSelect(item, index) {
                this.breadcrumbList.splice(index + 1, this.breadcrumbList.length - 1);
                this.$emit('on-bread-crumb-select', item);
            },
            onSetHeader() {
                if (_.isNil(this.title)) {
                    if (!this.breadcrumb) {
                        this.showHeader = false;
                    };
                };
            }
        }
    };
</script>
<style lang="less" scoped>
    .card-pannel {
        padding: 10px;
        position: relative;
        background-color: #fff;
        border-radius: 4px;
        -webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, .05), 0 2px 6px 0 rgba(0, 0, 0, .045);
        box-shadow: 0 1px 1px rgba(0, 0, 0, .05), 0 2px 6px 0 rgba(0, 0, 0, .045);
    }

    .card-pannel-padding0 {
        padding: 0px;
        position: relative;
        background-color: #fff;
        border-radius: 4px;
        -webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, .05), 0 2px 6px 0 rgba(0, 0, 0, .045);
        box-shadow: 0 1px 1px rgba(0, 0, 0, .05), 0 2px 6px 0 rgba(0, 0, 0, .045);
    }

    .card-header {
        cursor: pointer;
        padding-right: 75px;
        margin-bottom: 10px;

        &::after {
            content: '';
            position: absolute;
            width: 3px;
            background: linear-gradient(180deg, #25b6ff, #107fff);
            height: 18px;
            border-radius: 3px;
            top: 11px;
            margin-right: 0px;
        }


        .card-title {
            margin-left: 10px;
            font-size: 14px;
            font-weight: 700;
            line-height: 20px;
            position: relative;


        }
    }

    .card-content {
        position: relative;
    }

    .ivu-breadcrumb {
        margin-left: 10px;
    }
  
</style>