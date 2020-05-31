<template>
    <div class="sider-menu-collapsed">
        <template v-for="(item,index) in menuList">
            <div class="sider-menu-collapsed-panel">
                <template v-if="item.children && item.children.length > 0">
                    <sider-menu-drop :menu="item" @on-select="onMenuSelect"></sider-menu-drop>
                </template>
                <template v-else class="sider-menu-collapsed-panel">
                    <Tooltip transfer :content="item.name" placement="right" :key="index">
                        <a class="sider-menu-drop-a" :style="{textAlign: 'center'}" @click="onMenuSelect(item.menuId)">
                            <template v-if="item.menuIconProtCode=='img'">
                                <img :src="item.menuIconProtContent" />
                            </template>
                            <template v-else>
                                <i :class="item.menuIconProtContent"></i>
                            </template>
                        </a>
                    </Tooltip>
                </template>
            </div>
        </template>
    </div>
</template>
<script>
    import siderMenuDrop from '../sider-menu-drop';
    export default {
        name: 'siderMenuCollapsed',
        components: {
            siderMenuDrop
        },
        props: {
            menuList: {
                type: Array,
                default () {
                    return []
                }
            }
        },
        data() {
            return {

            };
        },
        mounted() {

        },
        methods: {
            onMenuSelect(menuId) {
                this.$emit('on-select', menuId);
            }
        }
    };
</script>
<style lang="less" scoped>
    .sider-menu-collapsed {
        .sider-menu-collapsed-panel {

            height: 60px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            margin-top: 0px;

            .sider-menu-drop-a {
                display: inline-block;
                padding: 8px 21px;
                width: 100%;
                text-align: center;
                color: #495060;

                i {
                    font-size: 20px;
                    color: white;
                }


                label {
                    font-size: 20px;
                    color: white;
                }
            }

        }

        .sider-menu-collapsed-panel:hover {
            background-color: #363E4F;

        }

    }
</style>