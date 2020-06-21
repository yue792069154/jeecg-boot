<template>
    <div style="width:100%; height:100%;">

        <router-view ref="routerView" v-show="showRouter"></router-view>
        <drawer-view ref="drawerView" v-show="showDrawer"></drawer-view>
        <modal-view ref="modalView" v-show="showModal"></modal-view>

    </div>
</template>
<script>
    import {
        MenuEntryProtEnum,
        MenuOpenTypeProtEnum
    } from "../../libs/fw-core/fw-constant/constant";

    import routerView from "./router-view";
    import modalView from "./modal-view";
    import drawerView from "./drawer-view";

    import appSettings from "../../common/appSettings";

    export default {
        components: {
            routerView,
            modalView,
            drawerView
        },
        data() {
            return {

                showRouter: false,
                showDrawer: false,
                showModal: false

            };
        },
        mounted() {

        },
        methods: {
            openPage(menu) {

                switch (menu.menuOpenTypeCode) {

                    case MenuOpenTypeProtEnum.INLINEPAGE:

                        this.showRouter = true;
                        this.showDrawer = false;
                        this.showModal = false;
                        this.$refs.routerView.openPage(menu);

                        break;

                    case MenuOpenTypeProtEnum.INLINEWINDOW:

                        this.showRouter = false;
                        this.showDrawer = false;
                        this.showModal = true;
                        this.$refs.modalView.openPage(menu);

                        break;

                    case MenuOpenTypeProtEnum.NEWWINDOW:

                        if (MenuEntryProtEnum.INTERNET == menu.menuEntryProtCode) {
                            window.open(menu.menuEntryProtContent);
                        } else if (MenuEntryProtEnum.COMPONENT == menu.menuEntryProtCode) {
                            window.open(appSettings.jeecg.webSiteRootUrl + (menu.menuRouteContent || menu.id));
                        }

                        break;

                    case MenuOpenTypeProtEnum.INLINESIDE:

                        this.showRouter = false;
                        this.showModal = false;
                        this.showDrawer = true;
                        this.$refs.drawerView.openPage(menu);

                        break;

                    default:

                        this.showRouter = true;
                        this.showDrawer = false;
                        this.showModal = false;
                        this.$refs.routerView.openPage(menu);

                        break;
                }
            }

        }
    }
</script>
<style lang="less">

</style>