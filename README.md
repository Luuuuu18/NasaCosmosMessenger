# 🌌 NASA Cosmos Messenger

<p align="center">
  <img width="100" height="105" alt="Nova Logo" src="https://github.com/user-attachments/assets/a7e77ff1-7abf-4c72-ab7b-100fae2d1980" />
</p>

## 專案介紹

NASA Cosmos Messenger 是一款以聊天互動為核心的 Android 應用程式。使用者可透過模擬 AI 助手 **Nova** 的對話方式，查詢 NASA APOD（Astronomy Picture of the Day）天文圖片與內容。

本專案整合聊天介面、NASA API 串接、本地收藏、圖片分享與日期選擇器等功能讓使用者能以更直覺、更有互動感的方式探索每日天文圖像。
[APK下載](https://drive.google.com/file/d/1Cvq8SDRqggnxVpELA-HVh-biYTkgdx05/view?usp=sharing)

---

## 展示影片
此專案操作展示影片如下，內容包含聊天查詢、日期輸入、收藏功能與分享星空卡功能：  
[觀看展示影片](https://youtube.com/shorts/J8-7Pu_YkYc?si=QN-ZzmyBn8_9_aee)

---

## 已完成功能總結
- [x] 聊天式 APOD 查詢
- [x] 輸入一般文字時顯示歡迎訊息與今日 APOD
- [x] 指定日期 APOD 查詢
- [x] 日期格式辨識與轉換
- [x] DatePicker 日期選擇器
- [x] 未來日期與最早日期限制檢查
- [x] Room 本地收藏功能
- [x] 收藏頁雙欄卡片 UI
- [x] 點擊收藏卡片查看完整內容
- [x] 長按聊天訊息加入收藏
- [x] 分享星空卡功能
- [x] 機器人頭像與聊天式互動 UI
- [x] API / 網路錯誤處理
- [x] OkHttp timeout 設定

<p align="center">
  <img width="1920" height="521" alt="Demo" src="https://github.com/user-attachments/assets/09a33bc2-6bf5-4eb7-869a-c57018afb022" />
</p>

---
## 專案架構樹狀圖

```bash
NASA Cosmos Messenger
├─ component/
│  ├─ ChatBubble.kt          # 聊天訊息泡泡，顯示使用者與 Nova 的對話內容
│  ├─ FavoriteCard.kt        # 收藏頁卡片元件，負責顯示 APOD 縮圖、標題與操作按鈕
│  ├─ InputBar.kt            # 聊天輸入列，包含文字輸入、送出與日期選擇入口
│  └─ ShareCard.kt           # 分享星空卡的版型，用於產生可分享圖片
│
├─ data/
│  ├─ local/
│  │  ├─ AppDatabase.kt          # Room 資料庫主體設定
│  │  ├─ AppDatabaseProvider.kt  # 提供單例資料庫實例，避免重複建立 DB
│  │  ├─ FavoriteDao.kt          # 收藏資料的新增、查詢、刪除操作
│  │  └─ FavoriteEntity.kt       # 收藏資料表欄位定義
│  │
│  ├─ remote/
│  │  ├─ ApodApi.kt          # Retrofit / OkHttp 設定與 NASA API 入口
│  │  ├─ ApodApiService.kt   # NASA APOD API 介面定義
│  │  └─ ApodResponse.kt     # API 回傳資料格式
│  │
│  └─ repository/
│     └─ FavoriteRepository.kt   # 統整收藏資料來源，提供 ViewModel 使用
│
├─ model/
│  └─ ChatMessage.kt         # 聊天訊息資料模型
│
├─ navigation/
│  ├─ BottomNavBar.kt        # 底部導航列
│  └─ MainScreen.kt          # 主畫面導航與頁面切換入口
│
├─ screen/
│  ├─ NovaChatScreen.kt      # 聊天室主畫面
│  └─ FavoritesScreen.kt     # 收藏頁主畫面
│
├─ util/
│  ├─ BitmapUtil.kt          # View / Compose 畫面轉 Bitmap 的工具函式
│  └─ ShareUtil.kt           # 分享圖片的工具函式
│
├─ viewmodel/
│  ├─ NovaViewModel.kt           # 聊天流程、日期判斷、API 查詢邏輯
│  ├─ FavoriteViewModel.kt       # 收藏頁資料狀態管理
│  └─ FavoriteViewModelFactory.kt # 建立帶參數的 FavoriteViewModel
│
└─ MainActivity.kt           # App 入口，啟動畫面與主題設定
```

## 架構說明與選擇原因

本專案採用 **MVVM（Model - View - ViewModel）架構**，並搭配 Repository pattern，將畫面、邏輯與資料來源分離，提升可維護性與擴充性。

| 層級              | 內容                                         | 負責項目                                                                                  | 選擇原因                                                                                |
| --------------- | ------------------------------------------ | ------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------- |
| View（畫面層）       | Jetpack Compose                            | `NovaChatScreen`、`FavoritesScreen`、`ChatBubble`、`InputBar`、`FavoriteCard`、`ShareCard` | Compose 適合快速建立現代化 UI；狀態驅動畫面更新，和聊天式介面非常契合；元件化程度高，便於後續維護與功能擴充                         |
| ViewModel（邏輯層）  | `NovaViewModel`、`FavoriteViewModel`        | 聊天流程控制、日期文字解析、API 呼叫、收藏資料狀態管理、錯誤訊息回應                                                  | ViewModel 可讓 UI 與邏輯分離；可集中管理聊天狀態與資料流；避免將商業邏輯直接寫在畫面層                                  |
| Data Layer（資料層） | Retrofit + OkHttp、Room Database、Repository | 串接 NASA APOD API、儲存本地收藏資料、整合本地與遠端資料來源                                                 | Retrofit 適合處理 RESTful API；Room 適合本地資料保存，符合收藏需求；Repository 可隔離資料來源細節，讓 ViewModel 更單純 |

| 架構選擇        | 說明                                                                                                                   |
| ----------- | -------------------------------------------------------------------------------------------------------------------- |
| 為什麼選擇 MVVM？ | 聊天式 App 具有明確的 UI 狀態變化與資料流需求。MVVM 可讓 UI 專注於顯示、ViewModel 專注於狀態與邏輯、Data Layer 專注於資料存取。這樣的分工能讓專案更清楚、可維護性更高，也更適合未來擴充更多功能。 |

---

## 安裝與執行方式

### 1. 環境需求
執行本專案前，請先準備以下開發環境：

#### 需求內容
- Android Studio
- JDK 11 以上
- Android SDK 35
- 可使用的 Android 模擬器或實體 Android 裝置

---

### 2. 下載專案
可透過以下方式取得專案原始碼。

#### 方法一：使用 Git Clone
```bash
git clone https://github.com/你的帳號/NasaCosmosMessenger.git
```
至 GitHub 專案頁面下載 ZIP，解壓縮後再用 Android Studio 開啟。
#### 方法二：直接下載 ZIP
進入 GitHub 專案頁面
下載 ZIP 壓縮檔
解壓縮後以 Android Studio 開啟

---

### 3. 開啟專案

下載完成後，使用 Android Studio 開啟專案。

#### 操作方式
- 開啟 Android Studio
- 選擇 **Open**
- 選取 `NasaCosmosMessenger` 專案資料夾

---

### 4. 同步 Gradle

開啟專案後，Android Studio 通常會自動執行 Gradle Sync。若未自動同步，可手動進行。

#### 操作方式
- 點選 **Sync Project with Gradle Files**

---

### 5. 設定 NASA API Key

本專案使用 NASA APOD API，因此需要先申請 API Key。

#### 申請位置
- `https://api.nasa.gov/`

#### 修改位置
請開啟以下檔案：

```text
app/src/main/java/com/example/nasacosmosmessenger/viewmodel/NovaViewModel.kt
```

找到以下程式碼：

```bash
private val apiKey = "YOUR_API_KEY"
```

將 YOUR_API_KEY 替換成自己申請的 NASA API Key。

### 6. 執行專案

完成 API Key 設定後，即可執行專案。

#### 執行方式
- 使用 Android Emulator 執行
- 或使用實體 Android 手機執行（需開啟 USB 偵錯）

#### Android Studio 操作
- 點選 **Run > Run 'app'**

---

### 7. 功能測試方式

成功啟動 App 後，可依序測試以下功能。

#### 可測試內容
- 輸入一般文字 → 顯示歡迎訊息與今日 APOD
- 輸入日期 → 查詢指定日期 APOD
- 點擊日期按鈕 → 開啟 DatePicker
- 長按聊天中的 APOD 訊息 → 加入收藏
- 進入收藏頁 → 查看收藏內容
- 點擊收藏卡片 → 查看完整內容
- 點擊分享按鈕 → 分享星空卡

---

### 8. 注意事項

執行專案時，需特別留意以下事項。

#### 注意內容
- NASA API 有請求次數限制，短時間內查詢過多可能出現 `403` 或 `429`
- 查詢日期不可晚於今天
- APOD 最早可查日期為 `1995-06-16`
- 分享功能需正確設定 `FileProvider`

---

### 9. 常見問題

#### 無法查詢 APOD
請確認：
- API Key 是否正確
- 裝置是否已連上網路
- 日期是否早於 `1995-06-16`
- 是否查詢了未來日期

#### 分享功能失敗
請確認：
- `AndroidManifest.xml` 已正確設定 `FileProvider`
- `res/xml/file_paths.xml` 已建立
- 裝置中有可接收分享的應用程式
---

## 核心功能說明

### 1. 聊天式查詢 APOD
使用者可直接在聊天室輸入文字與 Nova 互動。

#### 輸入非日期文字
系統會回覆：
- 歡迎訊息
- 今日 APOD
- 對應圖片與資訊

#### 輸入日期
系統會回覆：
- 「那天宇宙長這樣...」
- 該日期的 APOD 圖片與內容

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

## API 使用說明

本專案使用 NASA 官方 APOD API：  
`https://api.nasa.gov/planetary/apod`

### 功能包含
- 查詢今日 APOD
- 查詢指定日期 APOD
- 判斷圖片或影片類型
- 顯示圖片網址或說明文字

---

## 收藏功能

使用者可透過**長按聊天中的 APOD 訊息**加入收藏。

目前已支援：
- 長按加入收藏
- 收藏資料寫入 Room Database
- 收藏頁以雙欄卡片形式顯示
- 點擊卡片查看完整內容
- 可刪除收藏

---

## 完成的 Bonus 功能說明

### 1. 收藏本地儲存
本專案已完成使用 **Room Database** 儲存收藏資料。

#### 已完成內容
- 收藏資料會保存在本機
- 重新開啟 App 後仍可查看已收藏內容
- 收藏頁以卡片形式顯示資料

---

### 2. 分享星空卡
本專案已完成分享星空卡功能。

#### 功能內容
- 將收藏的 APOD 生成分享用卡片
- 卡片內容包含：
  - 天文圖片
  - 標題
  - 日期
- 透過 Android 分享機制，可將圖片分享至其他應用程式

#### 實作方式
- 使用 Compose 建立 `ShareCard` UI
- 將分享卡片轉換為 Bitmap
- 使用 `FileProvider` 處理檔案授權
- 透過 `Intent.ACTION_SEND` 呼叫系統分享功能

#### Bonus 價值
此功能展示了以下整合能力：
- Compose UI 建構
- Bitmap 轉換
- Android 原生分享機制
- FileProvider 檔案授權管理

---

## 額外完成的功能

除了題目基本要求與 Bonus 功能外，另外完成了以下提升使用體驗的功能：

### 1. DatePicker 日期選擇器
使用者可直接點擊輸入框旁的日期按鈕選取日期，減少手動輸入錯誤。

### 2. 機器人頭像聊天介面
Nova 回覆時會搭配機器人頭像，讓聊天介面更清楚區分使用者與系統訊息。

### 3. 收藏詳細內容 Dialog
點擊收藏卡片後，可開啟放大視窗查看完整圖片與全文說明。

### 4. 錯誤處理機制
已處理以下常見情況：
- 日期格式錯誤
- 查詢未來日期
- 日期早於 APOD 最早資料日
- 網路無法連線
- 連線逾時
- API 請求過多

### 5. API 連線優化
使用 OkHttp 設定 timeout，避免請求過久造成使用體驗不佳。

### 6. 收藏頁 UI 美化
收藏頁採用雙欄卡片式排列，並搭配圖片滿版、圓角與卡片式視覺設計，提升可讀性與整體質感。

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

## UI 設計說明
本專案採用簡潔的聊天式設計，重點包含：
- 聊天泡泡介面
- Nova 機器人頭像
- 日期選擇器
- 收藏頁雙欄卡片布局
- 點擊卡片查看完整內容
- 分享星空卡視覺化設計
