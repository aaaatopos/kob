<template>
    <ContentField>
        <table class="table table-hover">
            <thead>
                <tr>
                    <th>A</th>
                    <th>B</th>
                    <th>对战结果</th>
                    <th>对战时间</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="record in records" :key="record.record.id">
                    <td>
                        <img :src="record.a_photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username">{{ record.a_username }}</span>
                    </td>
                    <td>
                        <img :src="record.b_photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username">{{ record.b_username }}</span>
                    </td>
                    <td>{{ record.result }}</td>
                    <td>{{ record.record.createTime }}</td>
                    <td>
                        <button @click="open_record_content(record.record.id)" type="button" class="btn btn-secondary">查看录像</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <nav aria-label="...">
            <ul class="pagination" style="float: right;">
                <li class="page-item">
                    <a class="page-link" href="#" @click="click_page(-2)">前一页</a>
                </li>
                <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
                    <a class="page-link" href="#">{{ page.number }}</a>
                </li>
               
                <li class="page-item">
                    <a class="page-link" href="#" @click="click_page(-1)">后一页</a>
                </li>
            </ul>
        </nav>
    </ContentField>
</template>


<script>
import ContentField from '@/components/ContentField.vue'
import { useStore } from 'vuex';
import { ref } from 'vue';
import $ from 'jquery'
import router from '@/router/index';

export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        let records = ref([])
        let current_page = 1;
        let total_records = 0;

        let pages = ref([]);

        const click_page = page => {
            if(page === -2) page = current_page - 1;  // 前一页
            else if(page === -1) page = current_page + 1;  // 后一页
            let max_pages = parseInt(Math.ceil(total_records / 10));

            if(page >= 1 && page <= max_pages) {
                pull_page(page);
            }

        }

        const update_pages = () => {
            let max_pages = parseInt(Math.ceil(total_records / 10));
            let new_pages = [];
            for(let i = current_page - 2; i <= current_page + 2; i ++ ){
                if(i >= 1 && i <= max_pages) {
                    new_pages.push({
                        number: i,
                        is_active: i === current_page ? "active" : "",
                    });
                }
            }
            pages.value = new_pages;
        }

        const pull_page = pageNum => {
            current_page = pageNum;
            $.ajax({
                url: "http://127.0.0.1:3000/record/getlist/",
                data: {
                    "pageNum": pageNum,
                    "pageSize": 10,
                },
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    records.value = resp.records;
                    total_records = resp.records_count;
                    update_pages();
                },
                error(resp) {
                    console.log(resp);
                }
            });
        }

        pull_page(current_page);

        // 将地图从String转为数组
        const StringTo2D = map => {
            let g = [];
            for(let i = 0, k = 0; i < 13; i ++) {
                let line = [];
                for(let j = 0; j < 14; j ++, k ++) {
                    if(map[k] === '0') line.push(0);
                    else line.push(1);
                }
                g.push(line);
            }
            console.log(g);
            return g;
        }


        const open_record_content = recordId => {
            // 这边可以直接点击按钮是将当前的record对象传过来就可。
            for(const record of records.value) {  // 在records列表中找到当前的record
                if(record.record.id === recordId) {
                    store.commit("updateIsRecord", true);
                    console.log(record.record);
                    store.commit("updateGame", {
                        map: StringTo2D(record.record.map),
                        a_id: record.record.aid,
                        a_sx: record.record.asx,
                        a_sy: record.record.asy,
                        b_id: record.record.bid,
                        b_sx: record.record.bsx,
                        b_sy: record.record.bsy,
                    });
                    store.commit("updateSteps", {
                        a_steps: record.record.asteps,
                        b_steps: record.record.bsteps,
                    });
                    store.commit("updateRecordLoser", record.record.loser);
                    router.push({
                        name: "record_content",
                        params: {
                            recordId,
                        }
                    })
                    break;
                }
            }
        }

        return {
            records,
            total_records,
            open_record_content,
            pages,
            click_page,
        }
    }
}

</script>

<style scoped>

img.record-user-photo {
    width: 4vh;
    border-radius: 50%;
}
th, td {
    text-align: center;
    vertical-align: middle;  /* 垂直居中 */
}

</style>