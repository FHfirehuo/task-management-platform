import Vue from "vue";
import Router from "vue-router";
import Layout from "@/views/layout/Layout.vue";

Vue.use(Router);

/*
  redirect:                      if `redirect: noredirect`, it won't redirect if click on the breadcrumb
  meta: {
    title: 'title'               the name showed in subMenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon showed in the sidebar
    breadcrumb: false            if false, the item will be hidden in breadcrumb (default is true)
    hidden: true                 if true, this route will not show in the sidebar (default is false)
  }
*/

export default new Router({
  // mode: 'history',  // Disabled due to Github Pages doesn't support this, enable this if you need.
  scrollBehavior: (to, from, savedPosition) => {
    if (savedPosition) {
      return savedPosition;
    } else {
      return { x: 0, y: 0 };
    }
  },
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/",
      component: Layout,
      redirect: "/dashboard",
      name: "dashboard",
      meta: { hidden: true },
      children: [
        {
          path: "dashboard",
          component: () =>
            import(
              /* webpackChunkName: "dashboard" */ "@/views/dashboard/index.vue"
            )
        }
      ]
    },
    {
      path: "/example",
      component: Layout,
      redirect: "/example/table",
      name: "example",
      meta: { title: "用例", icon: "example" },
      children: [
        {
          path: "table",
          name: "Table",
          component: () =>
            import(/* webpackChunkName: "table" */ "@/views/table/index.vue"),
          meta: { title: "列表", icon: "table" }
        },
        {
          path: "tree",
          name: "Tree",
          component: () =>
            import(/* webpackChunkName: "tree" */ "@/views/tree/index.vue"),
          meta: { title: "树", icon: "tree" }
        }
      ]
    },
    {
      path: "/task-add",
      component: Layout,
      children: [
        {
          path: "index",
          name: "add",
          component: () =>
            import(/* webpackChunkName: "form" */ "@/views/form/index.vue"),
          meta: { title: "任务添加", icon: "form" }
        }
      ]
    },
    {
      path: "/task-list",
      component: Layout,
      children: [
        {
          path: "index",
          name: "list",
          component: () =>
            import(/* webpackChunkName: "form" */ "@/views/table/index.vue"),
          meta: { title: "任务列表", icon: "table" }
        }
      ]
    },

    {
      path: "external-link",
      component: Layout,
      children: [
        {
          path: "https://github.com/Armour/vue-typescript-admin-template",
          meta: { title: "External Link", icon: "link" }
        }
      ]
    }
  ]
});