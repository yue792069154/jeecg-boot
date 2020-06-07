<template>
    <span class="sider-menu-item">
        <template v-for="(item,index) in menuList">
            <template v-if="item.children&&item.children.length==0">
                <Menu-Item :name="item.id" :key="index">

                    <template v-if="item.menuIconProtCode=='img'">
                        <img :src="item.menuIconProtContent" />
                    </template>
                    <template v-else>
                        <i :class="item.menuIconProtContent"></i>
                    </template>

                    <label :class="item.menuClass" :style="item.menuStyle" v-text="item.menuName"></label>

                </Menu-Item>
            </template>
            <template v-else>
                <Submenu :name="item.id" :key="index">
                    <template slot="title">

                        <template v-if="item.menuIconProtCode=='img'">
                            <img :src="item.menuIconProtContent" />
                        </template>
                        <template v-else>
                            <i :class="item.menuIconProtContent"></i>
                        </template>

                         <label :class="item.menuClass" :style="item.menuStyle" v-text="item.menuName"></label>

                    </template>
                    <template v-if="item.children && item.children.length === 0">
                        <template v-for="(child,index) in item.children">
                            <Menu-Item :name="child.id" :key="index">

                                <template v-if="child.menuIconProtCode=='img'">
                                    <img :src="child.menuIconProtContent" />
                                </template>
                                <template v-else>
                                    <i :class="child.menuIconProtContent"></i>
                                </template>

                                 <label :class="child.menuClass" :style="child.menuStyle"
                                        v-text="child.menuName"></label>

                            </Menu-Item>
                        </template>
                    </template>
                    <template v-else>
                        <sider-menu-item :menuList="item.children" @on-select="onMenuSelect"></sider-menu-item>
                    </template>
                </Submenu>
            </template>
        </template>
    </span>
</template>
<script>
    export default {
        name: "siderMenuItem",
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
            onMenuSelect(id) {
                this.$emit('on-select', id);
            }
        }
    };
</script>
<style lang="less" scoped>
    .sider-menu-item {
        .ivu-menu-submenu img {
            width: 16px;
            height: 16px;
        }
    }
</style>