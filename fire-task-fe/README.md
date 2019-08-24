# fire-task-fe

## Project setup

```bash
yarn install
```

### Compiles and hot-reloads for development

```bash
yarn serve
```

### Compiles and minifies for production

```bash
yarn build
```

node_modules\element-ui\packages\theme-chalk\src\drawer.scss
elementui 的 2.11.0 报错不能使用原因是缺少分号

应该把

```css
@include drawer-animation(rtl) @include drawer-animation(ltr) @include
  drawer-animation(ttb) @include drawer-animation(btt);
```

改为

```css
@include drawer-animation(rtl);
@include drawer-animation(ltr);
@include drawer-animation(ttb);
@include drawer-animation(btt);
```
