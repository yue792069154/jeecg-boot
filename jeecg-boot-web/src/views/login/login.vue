<template>
    <Form ref="formInline" :model="formInline" inline>
        <FormItem prop="user">
            <Input type="text" v-model="formInline.user" placeholder="Username">
            <Icon type="ios-person-outline" slot="prepend"></Icon>
            </Input>
        </FormItem>
        <FormItem prop="password">
            <Input type="password" v-model="formInline.password" placeholder="Password">
            <Icon type="ios-lock-outline" slot="prepend"></Icon>
            </Input>
        </FormItem>
        <FormItem prop="captcha">
            <Input type="captcha" v-model="formInline.captcha" placeholder="captcha">
            </Input>
            <verification-code :remote="true" @success="generateCode" ref="verificationCode"></verification-code>
        </FormItem>
        <FormItem>
            <Button type="primary" @click="handleSubmit('formInline')">Signin</Button>
        </FormItem>
    </Form>
</template>

<script>
    import Vue from 'vue';

    import verificationCode from "../../components/verification-code";


    export default {
        components: {
            verificationCode
        },
        data() {
            return {
                formInline: {
                    user: "admin",
                    password: "Yue792069154!",
                    captcha: ""
                }
            }
        },
        created() {

        },
        methods: {
            handleSubmit: function () {

                let checkParams = this.$refs.verificationCode.getLoginParam();
                this.formInline.captcha=checkParams.checkCode;
                if (checkParams.checkCode.toLowerCase() === this.formInline.captcha.toLowerCase()) {
                    var loginInfo = {
                        userName: this.formInline.user,
                        password: this.formInline.password,
                        captcha: checkParams.checkCode,
                        checkKey: checkParams.checkKey
                    };

                    this.$store.dispatch('login', loginInfo).then(response=>{
                        this.$router.push('/home')
                    })
                }

            }



        },
        generateCode(value) {
            this.verifiedCode = value.toLowerCase()
        }

    }
</script>

<style lang="less" scoped>

</style>