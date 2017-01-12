用来测试hibernate相关功能的小项目。

# 命名规范

* 在单向关联映射中，会在实体类名和映射文件名后加One后缀，而双向关联则不加任何后缀，如CategoryOne与ItemOne是单向的多对多关联，而Category与Item是双向的多对多关联，当然这也可以从包名中看出；