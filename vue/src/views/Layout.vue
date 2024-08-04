<template>
    <div>
        <!--头部区域-->
        <div style="line-height: 60px;background-color: white;margin-bottom: 2px;display: flex">
            <div style="width: 300px">
                <img src="@/assets/logo.png" style="width: 40px;position: relative;top: 10px;left: 20px">
                <span style="margin-left: 25px;font-size:24px">图书管理系统</span>
            </div>
            <div style="flex: 1;text-align: right;padding-right: 20px">
                <el-dropdown size="medium">
                    <i class="el-icon-user-solid"></i>
                    <span class="el-dropdown-link" style="cursor: pointer">
                     {{ admin.username }}<i class="el-icon-arrow-down el-icon--right"></i>
                  </span>
                    <el-dropdown-menu slot="dropdown" style="margin-top: -5px">
                        <el-dropdown-item>
                            <div style="width: 60px;text-align: center" @click="logout">退出账号</div>
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>

            </div>
        </div>

        <!--侧边栏和主体-->
        <div style="display: flex;overflow: hidden">
            <!--侧边栏导航-->
            <div style="width: 180px;min-height:95vh;overflow: hidden;margin-right: 2px;background-color: white;margin-bottom: 2px">
                <el-menu :default-active="$route.path === '/' ? $route.path : $route.path.substring(1)"
                         :default-opens="['/']" router class="el-menu-demo">
                    <el-menu-item index="/">
                        <i class="el-icon-pie-chart"></i>
                        <span>首页</span>
                    </el-menu-item>

                    <el-submenu index="borrow">
                        <template slot="title">
                            <i class="el-icon-document-remove"></i>
                            <span>借书管理</span></template>
                        <el-menu-item index="/addBorrow">
                            <i class="el-icon-circle-plus-outline"></i>
                            <span>借书添加</span>
                        </el-menu-item>
                        <el-menu-item index="/borrowList">
                            <i class="el-icon-set-up"></i>
                            <span>借书列表</span>
                        </el-menu-item>

                    </el-submenu>
                    <el-submenu index="returns">
                        <template slot="title">
                            <i class="el-icon-document-add"></i>
                            <span>还书管理</span></template>
                        <el-menu-item index="/returnsList">
                            <i class="el-icon-set-up"></i>
                            <span>还书列表</span>
                        </el-menu-item>
                    </el-submenu>

                    <el-submenu index="book">
                        <template slot="title">
                            <i class="el-icon-collection"></i>
                            <span>图书管理</span></template>
                        <el-menu-item index="/addBook">
                            <i class="el-icon-circle-plus-outline"></i>
                            <span>图书添加</span>
                        </el-menu-item>
                        <el-menu-item index="/bookList">
                            <i class="el-icon-set-up"></i>
                            <span>图书列表</span>
                        </el-menu-item>
                    </el-submenu>

                    <el-submenu index="user">
                        <template slot="title">
                            <i class="el-icon-user"></i>
                            <span>会员管理</span></template>
                        <el-menu-item index="/adduser">
                            <i class="el-icon-circle-plus-outline"></i>
                            <span>会员添加</span>
                        </el-menu-item>
                        <el-menu-item index="/userList">
                            <i class="el-icon-set-up"></i>
                            <span>会员列表</span>
                        </el-menu-item>

                    </el-submenu>
                    <el-submenu index="admin">
                        <template slot="title">
                            <i class="el-icon-coordinate"></i>
                            <span>管理员管理</span></template>
                        <el-menu-item index="/addAdmin">
                            <i class="el-icon-circle-plus-outline"></i>
                            <span>管理员添加</span>
                        </el-menu-item>
                        <el-menu-item index="/adminList">
                            <i class="el-icon-set-up"></i>
                            <span>管理员列表</span>
                        </el-menu-item>
                    </el-submenu>

                    <el-submenu index="category">
                        <template slot="title">
                            <i class="el-icon-s-operation"></i>
                            <span>图书分类管理</span></template>
                        <el-menu-item index="/addCategory">
                            <i class="el-icon-circle-plus-outline"></i>
                            <span>图书分类添加</span>
                        </el-menu-item>
                        <el-menu-item index="/categoryList">
                            <i class="el-icon-set-up"></i>
                            <span>图书分类列表</span>
                        </el-menu-item>

                    </el-submenu>
                </el-menu>

            </div>
            <!--主体布局-->
            <div style="flex: 1; width:0; background-color: white;padding:10px;overflow-x: auto">
                <div style="min-width: 900px;">
                    <router-view/>
                </div>
            </div>
        </div>
    </div>

</template>

<script>
import Cookies from 'js-cookie'

export default {
    name: "Layout",
    data() {
        return {
            admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {}
        }
    },
    methods: {
        logout() {
            //清楚浏览器用户数据
            Cookies.remove('admin')
            this.$router.push('/login')
        }
    }
}
</script>

<style scoped>

</style>