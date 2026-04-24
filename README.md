# 🌌 NASA Cosmos Messenger

## 專案介紹
NASA Cosmos Messenger 是一款以聊天互動為核心的 Android 應用程式，使用者可以透過模擬 AI 助手「Nova」的對話方式，查詢 NASA APOD（Astronomy Picture of the Day）天文圖片與內容。

本專案結合聊天介面、NASA API 串接、本地收藏與圖片分享功能，讓使用者不只可以查詢每日天文圖，也可以收藏喜歡的內容並生成分享用的星空卡。

---

## 功能特色

### 1. 聊天式查詢 APOD
使用者可直接輸入文字與 Nova 互動。

- 若輸入**非日期文字**，系統會回覆：
  - 歡迎訊息
  - 今日 APOD
- 若輸入**日期**，系統會回覆：
  - 「那天宇宙長這樣：」
  - 指定日期的 APOD 圖片與資訊

---

### 2. 支援日期格式
系統目前支援以下兩種日期格式：

- `YYYY/MM/DD`
- `YYYY-MM-DD`

例如：

- `1995/06/20`
- `1995-06-20`

---

### 3. 日期限制判斷
由於 NASA APOD 有資料範圍限制，因此系統會先進行基本檢查：

- 最早可查日期：`1995-06-16`
- 不可查詢未來日期

---

### 4. 收藏功能
使用者可透過長按聊天中的 APOD 訊息加入收藏。

目前已支援：

- 長按加入收藏
- 收藏頁顯示所有收藏項目
- 點擊收藏卡片查看完整內容
- 刪除收藏

---

### 5. 分享星空卡（Bonus）
專案額外實作分享功能，可將收藏的 APOD 轉為可分享的圖片卡片。

包含：

- 自訂 ShareCard UI
- Compose 畫面轉 Bitmap
- 使用 FileProvider 處理分享檔案
- 透過 Android Intent 分享到其他應用程式

---

## 專案架構

本專案採用 **MVVM（Model - View - ViewModel）架構**，將 UI、邏輯與資料來源分離，方便維護與擴充。

### 架構分層

- **View**
  - Jetpack Compose UI
  - 顯示聊天內容、收藏頁、分享卡片
- **ViewModel**
  - 處理聊天邏輯、日期判斷、收藏資料狀態
- **Data Layer**
  - Retrofit 串接 NASA API
  - Room Database 儲存收藏資料
  - Repository 作為資料中介層

---

## 使用技術

- Kotlin
- Jetpack Compose
- MVVM
- Retrofit
- OkHttp
- Room Database
- Coil
- StateFlow
- FileProvider

---

## API 使用

本專案使用 NASA 官方 APOD API：

`https://api.nasa.gov/planetary/apod`

### 查詢方式

- 今日 APOD
- 指定日期 APOD

### 注意事項
NASA API 有免費請求限制，若短時間內請求過多，可能出現：

- `403`
- `429`

因此專案中也加入了錯誤訊息提示。

---

## 錯誤處理
系統目前已處理以下常見錯誤情況：

- 日期格式不正確
- 查詢未來日期
- 日期早於 APOD 最早日期
- 網路無法連線
- 連線逾時
- API 請求過多

---

## UI 設計
本專案介面以簡潔、聊天式互動為主，包含：

- 聊天泡泡 UI
- 機器人頭像
- 日期選擇器
- 圖片滿版顯示
- 收藏頁雙欄卡片布局
- 點擊卡片放大查看詳細內容

---

## 已完成功能總結

- [x] 聊天式 APOD 查詢
- [x] 日期辨識與格式轉換
- [x] 今日 APOD 顯示
- [x] 指定日期 APOD 查詢
- [x] Room 收藏功能
- [x] 收藏頁 UI
- [x] 點擊查看詳細資訊
- [x] 分享星空卡
- [x] 基本錯誤處理
