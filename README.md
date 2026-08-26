# 大樓包裹管理平台（精簡版）

依據 `bpms-prd.pdf` 與專案內的原型頁面（`大樓包裹管理系統-精簡版/`）建置的 Spring Boot + PostgreSQL 網頁服務。

## 技術棧

- Java 21 + Spring Boot 3（Web MVC、Thymeleaf 伺服器渲染、Spring Data JPA、Spring Mail）
- PostgreSQL
- 拍照存證：純瀏覽器端 `getUserMedia` + `<canvas>`，無任何第三方套件
- 登入：自訂 Session 機制（非 Spring Security），密碼以 BCrypt 雜湊儲存

## 快速啟動（Docker，推薦）

```bash
docker compose up --build
```

會同時啟動 PostgreSQL 與本應用程式，完成後開啟 http://localhost:8080

## 本機開發啟動

需先安裝 Java 21 與一個可連線的 PostgreSQL（建立一個空的 `bpms` 資料庫即可，資料表由 Hibernate 自動建立）。

```bash
./mvnw spring-boot:run
```

可用環境變數覆寫連線設定：`DB_HOST` `DB_PORT` `DB_NAME` `DB_USER` `DB_PASSWORD`。

## Email 寄送設定（真實 SMTP，Gmail）

系統會在「包裹到貨」與「包裹領取完成」時寄送真實 Email，需設定以下環境變數：

- `MAIL_USERNAME`：你的 Gmail 帳號（例如 `you@gmail.com`）
- `MAIL_APP_PASSWORD`：Google 帳戶「應用程式密碼」（**不是**一般登入密碼，需先於 Google 帳戶啟用兩步驟驗證後在 [myaccount.google.com/apppasswords](https://myaccount.google.com/apppasswords) 產生）

若未設定，程式仍可正常運作，只是寄信會在後端 log 顯示失敗、畫面上仍會照常顯示「已寄送」（不影響領取流程）。

`docker compose` 啟動前可先在 shell 匯出：

```bash
export MAIL_USERNAME=you@gmail.com
export MAIL_APP_PASSWORD=xxxxxxxxxxxxxxxx
docker compose up --build
```

## 測試帳號

| 身分 | 帳號 | 密碼 |
|---|---|---|
| 管理員 | admin.lin | 12345678 |
| 管理員 | wu.zhihao | 12345678 |
| 住戶（皆同一組密碼） | 見下方 Email 清單 | resident123 |

住戶 Email：`yian.chen@example.com`（陳怡安）、`yating.chang@example.com`（張雅婷）等十位，詳見 `src/main/resources/data.sql`。

已預先建立 3 筆待領包裹（包裹碼 `4826` 本人領取測試、`7319` 已授權代領測試、`3504` 尚未設定代領人）與 1 筆已領取歷史紀錄，方便直接測試「包裹領取」流程。

## 相機拍照注意事項

瀏覽器只允許在 `https://` 或 `http://localhost` 這類安全來源下使用相機權限。以 `http://localhost:8080` 本機測試沒有問題；若要部署到正式主機，網站必須啟用 HTTPS，否則管理員頁面的拍照功能會被瀏覽器封鎖。

## 功能範圍與簡化說明

嚴格依照 PRD 與原型頁面實作，未在其中出現的功能一律不做，並額外簡化以下幾點：

- **委託代領授權**：依照使用者確認，同時支援兩種方式——① 住戶登入後於「我的包裹」頁面直接設定 ② 到貨通知信中的一次性「他人代領」連結，免登入即可設定（`/agent-authorize?token=...`）。
- **包裹外觀照片**：PRD 的 Class Diagram 有此欄位，但原型頁面的登記表單與畫面都沒有實際上傳介面，因此未實作上傳功能（身分查驗畫面上的包裹圖示為裝飾用色塊，非真實照片）。
- **管理員側邊欄**僅保留原型最終版本實際會用到的三項：包裹領取／登記新包裹／住戶資料（原型程式碼中殘留但畫面已不會顯示的「包裹管理」「領取紀錄」清單頁未實作）。
- 4 位數包裹碼僅在「目前待領取（Available）」的包裹之間保證不重複；已領取的包裹碼可再次派發給新包裹，符合 PRD 例外情境「查詢到已領取包裹」的資安防範情境。
