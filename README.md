# 🛫 Mathladder - AR Oyun Projesi
### Trello : https://trello.com/w/guncelkonularcalismaalani
## 📜 Proje Özeti
Bu proje, uçak içinde çocuklu ailelere yönelik bir eğlence sistemi olarak geliştirilmiştir. Android tabanlı artırılmış gerçeklik (AR) oyunu, çocukların uzun uçak yolculuklarında keyifli vakit geçirmesini sağlamayı hedefler. Kullanıcılar, matematiksel işlemleri çözerek ilerler ve başarıdan sonra ödüllerle karşılaşır.

---

## 🎯 Projenin Amacı
Uçak içi eğlence sistemlerinde çocuklara yönelik çözümlerin eksikliği nedeniyle bu proje geliştirilmiştir. Artırılmış gerçeklik tabanlı oyun ile:
- Çocukların eğlenmesi,
- Eğitici bir içerik sunulması,
- Ailelerin rahat bir yolculuk geçirmesi hedeflenmiştir.

---

## 🛠️ Kullanılan Teknolojiler ve Araçlar
- **Programlama Dili:** Kotlin
- **Geliştirme Ortamı:** Android Studio
- **AR Kütüphanesi:** SceneView
- **Grafikler:** Android Adaptive Icons, GLB 3D modelleri
- **Test Araçları:** Android Emulator, Fiziksel cihaz testleri
- **Veritabanı:** `SharedPreferences`
- **CI/CD:** GitHub Actions (opsiyonel)

---

## 🎯 Hedef Kitle
Bu proje, özellikle çocuklu aileleri hedef alır:
- **4-12 yaş arası çocuklar** için eğlenceli oyun mekanikleri,
- **Aileler için eğitici bir çözüm**,
- **Havayolu şirketleri** tarafından uçak içi eğlence sistemine entegre edilebilir.

---

## 📂 Proje Yapısı
### 1. **Ana Uygulama (MainActivity.kt)**
- Oyunun ana mekanikleri burada tanımlanır.
- AR sahnesi ve matematik işlemleri yönetilir.

### 2. **AR SceneView Kullanımı**
- **ARSceneView** ile artırılmış gerçeklik sahnesi oluşturulur.
- Dinamik olarak sahneye 3D matematik objeleri (sayılar ve operatörler) eklenir.

### 3. **Matematiksel İşlemler**
- Rastgele işlemler (toplama, çıkarma, çarpma, bölme) oluşturulur:
```kotlin
fun generateRandomOperation(current: Int): Pair<String, Int> {
    val operations = listOf("+", "-", "*", "/")
    // İşlem ve operand seçimi
}
```
### 4. **Kullanıcı Arayüzü**
- Uygulama, kullanıcı dostu bir arayüz sunar:
  - **TextField:** Kullanıcının yalnızca sayı girişi yapmasına olanak tanır. 
  - Yanlış girişler engellenir ve hata mesajı gösterilir.
  - Klavyede `Enter` tuşuna basıldığında cevap gönderilir ve doğruluğu kontrol edilir.

### 5. **Ödül Mekanizması**
- Kullanıcı 5 doğru cevap verdiğinde ekrandaki matematik işlemleri kaybolur ve bir "KAZANDINIZ!" mesajı görüntülenir.
- Kullanıcı, **"Oyunu Yeniden Başlat"** düğmesine tıklayarak oyunu sıfırlayabilir ve yeni bir oyun başlatabilir.
- Bu mekanizma, çocukların motivasyonunu artırmak için tasarlanmıştır.

### 6. **Adaptif Simgeler ve Açılış Ekranı**
- **Uygulama Simgesi:**
  - `res/mipmap-anydpi-v26/adaptive-icon.xml` dosyasından özelleştirilebilir.
  - `ic_launcher_background` ve `ic_launcher_foreground` ile arka ve ön katmanlar ayrı ayrı tasarlanır.
- **Yükleme Ekranı:**
  - Başlangıçta bir yükleme animasyonu gösterilir.
  - Kullanıcı deneyimini iyileştirmek için modern bir tasarım kullanılmıştır.

---

## 📊 Veritabanı Kullanımı
- Uygulama, kullanıcı ilerlemesini ve ayarları `SharedPreferences` kullanarak saklar.
- Basit bir key-value yapısı ile oyun durumunu ve başarı puanlarını tutar:
  ```kotlin
  val sharedPreferences = getSharedPreferences("GamePreferences", Context.MODE_PRIVATE)
  val editor = sharedPreferences.edit()
  editor.putInt("score", currentScore)
  editor.apply()
  ```
  ## 🎮 Uygulamanın Çalışma Adımları

### 1. Başlangıç
- Kullanıcı uygulamayı açtığında modern bir **açılış ekranı** görüntülenir.
- Açılış ekranında bir yükleme animasyonu yer alır, bu süreçte gerekli kaynaklar yüklenir.
- **"Başlat"** butonuna basıldığında oyun ekranı açılır ve AR sahnesi başlatılır.

### 2. Oyun
- Kullanıcı, AR sahnesinde bir matematik işlemiyle karşılaşır. Örneğin:
12+8=?
- Kullanıcı, işlemi çözerek yalnızca sayı girişi yapılabilen bir **TextField** üzerinde cevabını girer.
- Klavyeden `Enter` tuşuna basarak cevabını gönderebilir.
- Yanlış cevaplarda hata mesajı görüntülenir ve kullanıcıdan doğru cevabı girmesi beklenir.
- Doğru cevap verdiğinde bir sonraki matematik işlemi otomatik olarak oluşturulur.

### 3. Kazanma
- Kullanıcı 5 doğru cevap üst üste verdiğinde:
- Matematik işlemleri ve diğer oyun öğeleri ekrandan kaldırılır.
- Sadece **"KAZANDINIZ!"** mesajı gösterilir.
- **"Oyunu Yeniden Başlat"** butonuna basarak oyun sıfırlanabilir ve yeni bir tur başlatılabilir.

---


## 🔧 Sık Karşılaşılan Sorunlar ve Çözümleri

### 1. **AR Sahnesi Yüklenmiyor**
- **Olası Sebep:** Cihazda ARCore desteği eksik.
- **Çözüm:** Cihazın ARCore uyumlu olduğundan emin olun ve gerekli kütüphanelerin yüklü olduğundan emin olun.

### 2. **Matematik İşlemleri Yanlış Görüntüleniyor**
- **Olası Sebep:** İşlemleri oluşturan algoritmada hata olabilir.
- **Çözüm:** Kod içerisindeki `generateRandomOperation` fonksiyonunu kontrol edin ve işlemleri doğru bir şekilde oluşturduğundan emin olun.

### 3. **Performans Sorunları**
- **Olası Sebep:** Yüksek çözünürlüklü modeller cihazın performansını düşürebilir.
- **Çözüm:** Daha düşük çözünürlüklü 3D modeller kullanarak cihazın kaynaklarını optimize edin.

---



