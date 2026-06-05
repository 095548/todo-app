<script setup lang="ts">
import { ref, onMounted } from 'vue'

// タスクの型を定義
interface Task {
  id: number
  title: string
  deadlineAt: string
  status: string
  createdAt: string
  updatedAt: string
}

const tasks = ref<Task[]>([])

// APIからタスク一覧を取得
async function fetchTasks() {
  const response = await fetch('http://localhost:8080/tasks')
  tasks.value = await response.json()
}

onMounted(() => {
  fetchTasks()
})
</script>

<template>
  <div>
    <h1>タスク一覧</h1>
    <ul>
      <li v-for="task in tasks" :key="task.id">
        {{ task.title }} - {{ task.status }} - 期限: {{ task.deadlineAt }}
      </li>
    </ul>
  </div>
</template>

<style scoped>
div {
  max-width: 600px;
  margin: 50px auto;
}
li {
  margin: 8px 0;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
</style>
