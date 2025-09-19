# TANK1990
# Tank1990 Projesi

## Ödev Değerlendirmesi
- Oyunun başlangıç, oynanış ve bitiş akışı eksiksiz şekilde çalışmaktadır.  
- Oyun tek oyuncuyla oynanır ve sadece 1 seviyeden oluşmaktadır (**TANK1990 level 2**).  
- 20 düşman tank öldürüldüğünde kazanma ekranı düzgün çalışır.  
- Oyun, kartal öldürüldüğünde veya oyuncunun tüm canları bittiğinde sona erer.  
- Ses, skor sistemi, pause butonu, çarpışma sistemleri çalışmaktadır.  
- PowerUp sistemi dahil edilmiş ancak sonradan kaldırılmıştır.  

---

## Ödev Tasarımı (Kodların Açıklaması)
- **core.GamePanel**: Oyunun tüm ana kontrolünün yapıldığı, çizim, güncelleme ve klavye dinleme işlemlerinin gerçekleştiği ana sınıf.  
- **main.Game**: Oyunun giriş noktası. JFrame ayarları burada yapılır.  
- **map.GameMap**: Haritayı temsil eder, harita verilerini okur ve çizer.  
- **entity paketindeki sınıflar**:  
  - **PlayerTank**: Oyuncunun tankı, hareket ve ateşleme işlemleri.  
  - **BasicTank**, **FastTank**, **PowerTank**, **ArmorTank**: Düşman tanklarının türleri.  
  - **Bullet**: Tüm mermi işlemlerini içerir.  

### Notlar
- Her tank türü ayrı sınıfta tutulmuş, hız ve dayanıklılık özellikleri farklılaştırılmıştır.  
- Oyun döngüsü `GamePanel` içinde 60 FPS güncelleme sistemiyle sağlanmıştır.  
- Harita `map.txt` dosyası üzerinden kolayca düzenlenebilir.  
- Çizim işlemleri ile çarpışma kontrolleri ayrılmıştır.  
- Oyuncu hareketi ve mermi atışı yön tuşları + `Z` tuşu ile sağlanır.  
- 20 düşman tank öldürülünce oyun kazanılır (`totalEnemiesKilled`).  
- Kartal vurulduğunda oyun biter (`destroyEagle()`).  
- Ses desteği: Java `AudioSystem` ile `.wav` dosyaları.  
- Pause sistemi: JButton ile duraklatma/başlatma sağlanır.  

---

## Kaynaklar
- [YouTube Playlist](https://www.youtube.com/watch?v=om59cwR7psI&list=PL_QPQmz5C6WUF-pOQDsbsKbaBZqXj4qSq)  

## Demo Videosu
- [Proje Demo Videosu](https://youtu.be/zb4fVr6ncwM)
