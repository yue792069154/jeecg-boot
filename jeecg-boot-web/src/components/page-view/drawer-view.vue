<template>

    <Drawer :title="drawerTitle" :width="drawerWidth" v-model="showDrawer">
        <iframe v-if="showIframe" style="width:100%; height:100%;" :src="iframeSrc" frameborder="no"
            border="0"></iframe>
        <component v-if="!showIframe" :key="drawerComponentKey" :is="drawerComponent"></component>
    </Drawer>

</template>
<script>
    import {
        MenuEntryProtEnum
    } from "../../libs/fw-core/fw-constant/constant"
    export default {
        data() {
            return {

                showDrawer: true,
                showIframe: false,

                drawerTitle: "",
                drawerComponent: "",
                drawerComponentKey: "",
                drawerWidth: "600",

                iframeSrc: ""

            };
        },
        mounted() {

        },
        methods: {

            openPage(menu) {

                this.showDrawer = true;
                this.drawerTitle = menu.menuName;

                if (MenuEntryProtEnum.INTERNET == menu.menuEntryProtCode) {

                    this.showIframe = true;
                    this.iframeSrc = menu.menuEntryProtContent;

                } else if (MenuEntryProtEnum.COMPONENT == menu.menuEntryProtCode) {

                    this.showIframe = false;
                    this.drawerComponentKey = menu.id;
                    this.drawerComponent = this.$store.state.menuComponent[menu.id].default;

                }

            }

        }
    };
</script>
<style lang="less">
    ::-webkit-scrollbar-track {
        background-color: #F5F5F5;
    }

    ::-webkit-scrollbar {
        width: 4px;
        background-color: #F5F5F5;
    }

    ::-webkit-scrollbar-thumb {
        background-color: #dcdee2;
    }
</style>