// import { createApp } from 'vue'
// import App from './App.vue'
// import router from './router'

// createApp(App)
//   .use(router)   // 👈 关键
//   .mount('#app')

import { createApp } from 'vue'
import App from './App.vue'

// ✅ 引入 router
import router from './router'

// 引入 Element Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App)

app.use(ElementPlus)
app.use(router)   // ✅ 必须加这一行

app.mount('#app')