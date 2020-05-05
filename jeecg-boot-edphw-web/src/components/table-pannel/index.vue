<template>
    <div>
        <div class="table-alert">
            <slot name="alert"></slot>
        </div>
        <div class="table-filter">
            <card-pannel>
                <slot name="filter"></slot>
            </card-pannel>
        </div>
        <div class="table-action">
            <card-pannel class="padding-10">
                <div class="table-action-content">
                    <slot name="action"></slot>
                </div>
            </card-pannel>
        </div>
        <div class="table-summary">
            <slot name="summary"></slot>
        </div>
        <div class="table-grid">
            <slot name="grid"></slot>
        </div>
        <div class="table-page">
            <slot name="page"></slot>
        </div>
        <Drawer :styles="drawerStyles" v-model="showDrawer" :title="drawerTitle" :width="drawerWidth"
            class="table-drawer">
            <div class="table-drawer-content">
                <slot name="drawer"></slot>
            </div>
            <div class="table-drawer-footer">
                <Button style="margin-right: 8px" @click="showDrawer = false">取消</Button>
                <Button type="primary" @click="onOk">确定</Button>
            </div>
        </Drawer>
    </div>
</template>
<script>
    import cardPannel from '../card-pannel';
    export default {
        name: 'tablePannel',
        components: {
            cardPannel
        },
        props: {

        },
        data() {
            return {
                showDrawer: false,
                drawerTitle: "",
                drawerWidth: "500",
                drawerStyles: {
                    height: 'calc(100% - 55px)',
                    overflow: 'auto',
                    paddingBottom: '53px',
                    position: 'static'
                }
            }
        },
        mounted() {

        },
        methods: {
            onOk() {
                this.$emit("on-ok")
            }
        }
    };
</script>
<style lang="less" scoped>
    .table-alert {
        padding: 0px;
        margin-bottom: 10px;
    }

    .table-filter {
        padding: 0px;
        margin-bottom: 10px;
    }

    .table-action {
        padding: 0px;
        margin-bottom: 10px;
    }

    .table-summary {
        height: 32px;
        line-height: 31px;
        padding: 0px 8px;
        border: 1px solid #d4f0fc;
        background-color: #f0f8fd;
        font-size: 13px;
        border-bottom: 1px solid #48A6FD !important;
        color: #6B676E;

    }

    .table-grid {

        padding: 0px;
        position: relative;
        background-color: #fff;
        -webkit-box-shadow: 0 0px 0px rgba(0, 0, 0, .05), 0 0px 6px 0 rgba(0, 0, 0, .045);
        box-shadow: 0 0px 0px rgba(0, 0, 0, .05), 0 0px 6px 0 rgba(0, 0, 0, .045);

    }

    .table-page {
        text-align: right;
        background: white;
        border-bottom-right-radius: 4px;
        border-bottom-left-radius: 4px;
        padding: 8px 10px;
        line-height: 32px;
        margin-bottom: 10px;

        .table-page-action {
            float: left;
        }

        .ivu-icon-ios-arrow-back {
            line-height: 30px;
        }

        .ivu-icon-ios-arrow-forward {
            line-height: 30px;
        }

    }

    .table-drawer {

        position: relative;

        .table-drawer-content {
            padding: 0px;
        }

        .table-drawer-footer {
            width: 100%;
            position: absolute;
            bottom: 0;
            left: 0;
            border-top: 1px solid #e8e8e8;
            padding: 10px 16px;
            text-align: right;
            background: #fff;
        }
    }

    .table-action {

        .table-action-content{
          
        }

        .padding-10 {
            padding: 10px;
        }

        label {
            cursor: pointer;
            margin-right: 2px;
        }
    }
</style>