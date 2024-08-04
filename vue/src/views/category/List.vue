<template>
    <div>
        <!--      搜索表单-->
        <div style="margin-bottom: 35px;margin-top: 30px">
            <el-input style="width:240px" placeholder="请输入分类名称" v-model="params.name"></el-input>
            <el-button style="margin-left:5px" type="primary" @click="load"><i class="el-icon-search"></i> 搜索
            </el-button>
            <el-button style="margin-left:5px" type="warning" @click="reset"><i class="el-icon-refresh"></i> 重置
            </el-button>
        </div>


        <el-table :data="tableData" stripe row-key="id" default-expand-all>
            <el-table-column prop="id" label="编号" width="50"></el-table-column>
            <el-table-column prop="name" label="名称"></el-table-column>
            <el-table-column prop="remark" label="备注"></el-table-column>
            <el-table-column prop="createtime" label="创建时间"></el-table-column>
            <el-table-column prop="updatetime" label="更新时间"></el-table-column>

            <el-table-column label="操作" width="250px">
                <template v-slot="scope">
                    <!--                    scope.row 就是当前行数据-->
                    <div style="display: flex">
                        <el-button type="success" v-if="!scope.row.pid" @click="handleAdd(scope.row)">添加二级分类
                        </el-button>
                        <el-button type="primary" @click="$router.push('/editCategory?id='+scope.row.id)">编辑
                        </el-button>
                        <el-popconfirm
                                style="margin-left: 14px;"
                                confirm-button-text='YES'
                                cancel-button-text='NO'
                                icon="el-icon-info"
                                title="是否确定删除该行数据"
                                @confirm="del(scope.row.id)"
                        >
                            <el-button type="danger" slot="reference">删除</el-button>
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
        <el-dialog title="添加二级分类" :visible.sync="dialogFormVisible" width="30%">
            <el-form :model="form" label-width="100px" style="width: 85%">
                <el-form-item label="分类名称" prop="name">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="分类备注" prop="remark">
                    <el-input v-model="form.remark" autocomplete="off"></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>

import request from "@/utils/request";
import Cookies from 'js-cookie';

export default {
    name: 'CategoryList',
    data() {
        return {
            admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
            tableData: [],
            total: 0,
            dialogFormVisible: false,
            form: {},
            pid: null,
            params: {
                pageNumber: 1,
                pageSize: 10,
                name: '',
                remark: ''
            },

        }
    },
    created() {
        this.load()
    },
    methods: {
        load() {
            // fetch('http://localhost:9090/category/list').then(res=>res.json()).then(res=>{
            //     console.log(res)
            //     this.tableData=res
            // })
            request.get('/category/page', {
                params: this.params
            }).then(res => {
                if (res.code == '200') {
                    this.tableData = res.data.list
                    this.total = res.data.total
                }

            })
        },
        reset() {
            this.params = {
                pageNumber: 1,
                pageSize: 10,
                name: '',
                remark: ''
            }
            this.load()
        },
        del(id) {
            request.delete("/category/delete/" + id).then(res => {
                if (res.code == '200') {
                    this.$notify.success("删除成功")
                    this.load()
                } else
                    this.$notify.error(res.msg)
            });

        },
        //点击切换分页
        handleCurrentChanged(pageNumber) {
            this.params.pageNumber = pageNumber
            this.load()
        },
        handleAdd(row) {
            //将当前行的id作为二级分类的pid
            this.pid = row.id
            this.dialogFormVisible = true
        },
        save() {
            this.form.pid = this.pid
            request.post('/category/save', this.form).then(res => {
                if (res.code == '200') {
                    this.$notify.success('新增二级分类成功')
                    this.dialogFormVisible = false
                    this.load()
                } else {
                    this.$notify.error(res.msg)
                }

            })
        }

    }
}
</script>
