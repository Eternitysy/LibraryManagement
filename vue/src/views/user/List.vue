<template>
    <div>
        <!--      搜索表单-->
        <div style="margin-bottom: 35px;margin-top: 30px">
            <el-input style="width:240px" placeholder="请输入名称" v-model="params.name"> </el-input>
            <el-input style="width:240px;margin-left:5px" placeholder="请输入联系方式" v-model="params.phone"> </el-input>
            <el-button style="margin-left:5px" type="primary"@click="load"><i class="el-icon-search"></i> 搜索</el-button>
            <el-button style="margin-left:5px" type="warning"@click="reset"><i class="el-icon-refresh"></i> 重置</el-button>
        </div>


        <el-table :data="tableData" stripe>
            <el-table-column prop="id" label="编号" width="50"></el-table-column>
            <el-table-column prop="cardID" label="卡号"></el-table-column>
            <el-table-column prop="name" label="姓名"></el-table-column>
            <el-table-column prop="age" label="年龄"></el-table-column>
            <el-table-column prop="sex" label="性别" ></el-table-column>
            <el-table-column prop="address" label="地址"></el-table-column>
            <el-table-column prop="phone" label="联系方式"></el-table-column>
            <el-table-column prop="account" label="账户积分"></el-table-column>
            <el-table-column prop="createtime" label="创建时间"></el-table-column>
            <el-table-column prop="updatetime" label="更新时间"></el-table-column>
            <el-table-column  label="状态" >
                <template v-slot="scope">
                    <el-switch
                            v-model="scope.row.status"
                            @change="changeStatus(scope.row)"
                            active-color="#13ce66"
                            inactive-color="#ff4949">
                    </el-switch>
                </template>
            </el-table-column>

            <el-table-column  label="操作" width="200">
                <template v-slot="scope">
<!--                    scope.row 就是当前行数据-->
                    <div style="display: flex">
                    <el-button  type="success" @click="handleAccount(scope.row)">充值</el-button>
                    <el-button  type="primary" @click="$router.push('/editUser?id='+scope.row.id)">编辑</el-button>
                    <el-popconfirm
                            style="margin-left: 14px;"
                            confirm-button-text='YES'
                            cancel-button-text='NO'
                            icon="el-icon-info"
                            title="是否确定删除该行数据"
                            @confirm="del(scope.row.id)"
                    >
                    <el-button  type="danger" slot="reference">删除</el-button>
                    </el-popconfirm>
                    </div>
                </template>
            </el-table-column>

        </el-table>
        <!--      分页-->
        <div style="margin-top: 20px">
            <el-pagination
                    background
                    :current="params.pageNumber"
                    :page-size="params.pageSize"
                    layout="prev, pager, next"
                    @current-change="handleCurrentChanged"
                    :total="total">
            </el-pagination>
        </div>
        <el-dialog title="充值" :visible.sync="dialogFormVisible" width="30%">
            <el-form :model="form" label-width="100px" style="width: 85%" :rules="rules" ref="ruleForm">
                <el-form-item label="当前账户积分" prop="account">
                    <el-input disabled v-model="form.account" autocomplete="off" ></el-input>
                </el-form-item>
                <el-form-item label="充值积分" prop="score">
                    <el-input v-model="form.score" autocomplete="off" ></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="addAccount">充 值</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>

import request from "@/utils/request";

export default {
    name: 'User',
    data(){
        const checkNUms = (rule, value, callback) => {
            if (!/^[0-9]+$/.test(value)) {
                callback(new Error('请输入正整数值'));
            }
            if(parseInt(value)>200||parseInt(value)<10){
                callback(new Error("请输入10-200间的整数"))
            }
            callback()
        };
        return {
            tableData: [],
            total:0,
            params:{
                pageNumber:1,
                pageSize:10,
                name:'',
                phone:'',
                account:'15'
            },
            dialogFormVisible:false,
            form:{},
            rules: {
                score:[
                    { validator: checkNUms, trigger: 'blur' }
                ]

            }
        }
    },
    created() {
        this.load()
    },
    methods:{
        changeStatus(row){
            request.put('/user/update',row).then(res=>{
                if(res.code=='200'){
                    this.$notify.success('操作成功')
                    this.load()
                }
                else{
                    this.$notify.error(res.msg)
                }

            })

        },
        load(){
            // fetch('http://localhost:9090/user/list').then(res=>res.json()).then(res=>{
            //     console.log(res)
            //     this.tableData=res
            // })
            request.get('/user/page',{
                params: this.params
            }).then(res=>{
                if(res.code=='200') {
                    this.tableData = res.data.list
                    this.total = res.data.total
                }
            })
        },
        reset(){
            this.params = {
                pageNumber:1,
                pageSize:10,
                name:'',
                phone:'',
                account:'15'
            }
            this.load()
        },
        del(id){
            request.delete("/user/delete/"+id).then(res=>{
                if(res.code=='200') {
                    this.$notify.success("删除成功")
                    this.load()
                }
                else
                    this.$notify.error(res.msg)
            });

        },
        //点击切换分页
        handleCurrentChanged(pageNumber){
            this.params.pageNumber=pageNumber
            this.load()
        },
        handleAccount(row){
            this.form=JSON.parse(JSON.stringify(row))
            this.dialogFormVisible=true
        },
        addAccount(){
            this.$refs["ruleForm"].validate((valid) => {
                if (valid) {
                    request.post('/user/account',this.form).then(res=>{
                        if(res.code=='200') {
                            this.$notify.success("充值成功")
                            this.dialogFormVisible=false
                            this.load()
                        }
                        else
                            this.$notify.error(res.msg)
                    })
                }
            })


        }
    }
}
</script>
