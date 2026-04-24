# 🌌 NASA Cosmos Messenger

## 專案介紹
NASA Cosmos Messenger 是一款以聊天互動為核心的 Android 應用程式。使用者可透過模擬 AI 助手「Nova」的對話方式，查詢 NASA APOD（Astronomy Picture of the Day）天文圖片與內容。  
本專案整合聊天介面、NASA API 串接、本地收藏與分享星空卡功能，讓使用者能以更直覺的方式探索每日天文圖像。

---
## 展示影片
此專案操作展示影片如下，內容包含聊天查詢、日期輸入、收藏功能與分享星空卡功能：  
[觀看展示影片](https://youtube.com/shorts/J8-7Pu_YkYc?si=QN-ZzmyBn8_9_aee)
## 架構說明、選擇的原因

本專案採用 **MVVM（Model - View - ViewModel）架構**，並搭配 Repository pattern，將 UI、商業邏輯與資料來源分離。

### 架構分層

#### 1. View（畫面層）
使用 **Jetpack Compose** 建立畫面，包含：
- 聊天室畫面（NovaChatScreen）
- 收藏頁（FavoritesScreen）
- 聊天泡泡、輸入框、收藏卡片、分享卡片等 UI 元件

**原因：**
- Compose 適合快速建立現代化 UI
- 狀態驅動畫面更新，和聊天介面很契合
- 元件化程度高，方便後續維護與擴充

---

#### 2. ViewModel（邏輯層）
使用：
- `NovaViewModel`
- `FavoriteViewModel`

負責：
- 處理聊天室對話邏輯
- 判斷輸入是否為日期
- 串接 NASA API
- 控制收藏資料的新增、刪除與顯示狀態

**原因：**
- ViewModel 可以讓 UI 與邏輯分離
- 避免將 API 呼叫與狀態判斷直接寫在畫面中
- 更適合管理聊天狀態與收藏頁資料

---

#### 3. Data Layer（資料層）
包含：
- **Retrofit + OkHttp**：負責串接 NASA APOD API
- **Room Database**：負責儲存本地收藏資料
- **Repository**：整合 API 與本地資料來源

**原因：**
- Retrofit 適合處理 REST API
- Room 可提供本地資料儲存，符合收藏需求
- Repository 可隔離資料來源細節，讓 ViewModel 更單純

---

### 選擇 MVVM 的原因總結
本專案之所以選擇 MVVM，是因為聊天式應用具有明確的 UI 狀態變化與資料流動需求。  
MVVM 可讓：
- UI 專注於顯示
- ViewModel 專注於邏輯與狀態
- Data Layer 專注於 API 與本地資料

這樣的分工能讓專案更清楚、更容易維護，也更適合未來加入更多功能。

---

## 功能特色

### 1. 聊天式查詢 APOD
使用者可直接輸入文字與 Nova 互動。

- 若輸入**非日期文字**，系統會回覆：
  - 歡迎訊息
  - 今日 APOD
- 若輸入**日期**，系統會回覆：
  - 「那天宇宙長這樣...」
  - 該日期的 APOD 圖片與資訊

---

## 支援的日期格式列表

本專案目前支援以下日期格式：

- `YYYY/MM/DD`
- `YYYY-MM-DD`

### 範例
- `1995/06/20`
- `1995-06-20`

### 日期限制
- APOD 最早可查日期：`1995-06-16`
- 不可查詢未來日期

---

## API 使用
本專案使用 NASA 官方 APOD API：

`https://api.nasa.gov/planetary/apod`

### 功能
- 查詢今日 APOD
- 查詢指定日期 APOD
- 判斷圖片或影片類型

---

## 收藏功能
使用者可透過長按聊天中的 APOD 訊息加入收藏。

目前已支援：
- 長按加入收藏
- 收藏頁顯示所有收藏資料
- 點擊收藏卡片查看完整內容
- 刪除收藏

---

## 完成的 Bonus 功能說明

### 1. 分享星空卡
本專案已完成分享星空卡功能。

#### 功能內容
- 將收藏的 APOD 生成一張可分享的圖片卡片
- 卡片內容包含：
  - 天文圖
  - 標題
  - 日期
- 透過 Android 分享機制，可將星空卡分享至其他應用程式

#### 實作方式
- 使用 Compose 建立 `ShareCard` UI
- 將 Compose 畫面轉換為 Bitmap
- 使用 `FileProvider` 處理分享檔案
- 透過 `Intent.ACTION_SEND` 呼叫系統分享功能

#### Bonus 價值
這個功能除了展示 UI 製作能力，也整合了：
- Compose UI 建構
- Bitmap 產生
- Android 原生分享機制
- FileProvider 檔案授權管理

---

### 2. 收藏本地儲存
除了基本收藏功能外，也透過 Room 完成收藏資料的本地保存。

#### 已完成內容
- 收藏資料會儲存在本機 Room Database 中
- 重新開啟 App 後仍可瀏覽已收藏內容
- 收藏頁以卡片形式顯示資料

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

## 錯誤處理
系統目前已處理以下常見情況：
- 日期格式錯誤
- 查詢未來日期
- 日期早於 APOD 最早日期
- 網路無法連線
- 連線逾時
- API 請求過多

---

## UI 設計
本專案介面採用簡潔的聊天式設計，包含：
- 聊天泡泡介面
- Nova 機器人頭像
- 日期選擇器
- 收藏頁雙欄卡片布局
- 點擊收藏卡片查看完整內容
- 分享星空卡視覺化設計

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

---
