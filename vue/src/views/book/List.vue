<template>
    <div>
        <!--      搜索表单-->
        <div style="margin-bottom: 35px;margin-top: 30px">
            <el-input style="width:240px" placeholder="请输入书名" v-model="params.name"></el-input>
            <el-input style="width:240px;margin-left:5px" placeholder="请输入图书编号"
                      v-model="params.bookNo"></el-input>
            <el-button style="margin-left:5px" type="primary" @click="load"><i class="el-icon-search"></i> 搜索
            </el-button>
            <el-button style="margin-left:5px" type="warning" @click="reset"><i class="el-icon-refresh"></i> 重置
            </el-button>
        </div>


        <el-table :data="tableData" stripe>
            <el-table-column prop="id" label="编号" width="50"></el-table-column>
            <el-table-column prop="cover" label="封面">
                <meta name="referrer" content="no-referrer"/>
                <template v-slot="scope">
                    <el-image :src="scope.row.cover" :preview-src-list="[scope.row.cover]"></el-image>
                </template>
            </el-table-column>
            <el-table-column prop="name" label="书名"></el-table-column>
            <el-table-column prop="author" label="作者" width="80"></el-table-column>
            <el-table-column prop="publisher" label="出版商" width="70"></el-table-column>
            <el-table-column prop="publishDate" label="出版日期" width="70"></el-table-column>
            <el-table-column prop="bookNo" label="图书编号"></el-table-column>
            <el-table-column prop="category" label="分类"></el-table-column>
            <el-table-column prop="nums" label="数量"></el-table-column>
            <el-table-column prop="score" label="借书积分"></el-table-column>
            <el-table-column prop="description" label="内容简介" width="100"></el-table-column>


            <el-table-column label="操作" width="200px">
                <template v-slot="scope">
                    <!--                    scope.row 就是当前行数据-->
                    <div style="display: flex">
                        <el-button type="primary" @click="$router.push('/editBook?id='+scope.row.id)">编辑</el-button>
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
    </div>
</template>

<script>

import request from "@/utils/request";
import Cookies from "js-cookie";

export default {
    name: 'BookList',
    data() {
        return {
            admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
            tableData: [],
            total: 0,
            params: {
                pageNumber: 1,
                pageSize: 10,
                name: '',
                book_id: ''
            }
        }
    },
    created() {
        this.load()
    },
    methods: {
        load() {
            // fetch('http://localhost:9090/user/list').then(res=>res.json()).then(res=>{
            //     console.log(res)
            //     this.tableData=res
            // })
            request.get('/book/page', {
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
                book_id: ''
            }
            this.load()
        },
        del(id) {
            request.delete("/book/delete/" + id).then(res => {
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
        }
    }
}
</script>
