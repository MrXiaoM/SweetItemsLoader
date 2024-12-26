# SweetItemsLoader

ItemsAdder 3.6.4 字体图片字符变量。

## 简介

写这个插件主要是不想每个子服都安装 ItemsAdder，就算加软链接，每个子服都要重载一遍太麻烦了。特别是有 MythicMobs 的子服，要是 MythicMobs 配置的东西特别多，重载起来很慢。

我只想要用字体图片变量，还得把 ItemsAdder 那一堆给移过来，有点太浪费了。

这个插件会在有 ItemsAdder 的子服，在 ItemsAdder 重载的时候，保存字体图片字符列表到 `font_images.yml` 文件。  
在没有 ItemsAdder 的子服，读取保存的 `font_images.yml` 文件，注册 PAPI 变量 `img`，争取做到与 ItemsAdder 原本的变量基本一致。

## 用法

不确保本插件在其他版本的 ItemsAdder 能正常使用。升级 IA 把我升级怕了，能用就不敢升级了。

在各个子服安装本插件即可，有 ItemsAdder 的子服无需配置，没有 ItemsAdder 的子服需要到 `config.yml` 修改 `font_images_file` 的路径。

## 命令

+ `/sil gen` 生成 `font_images.yml` 并通知各子服重载
+ `/sil reload` 重载配置文件
