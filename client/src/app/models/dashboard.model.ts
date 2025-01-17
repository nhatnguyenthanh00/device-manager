export interface SidebarItem {
    id: string;
    label: string;
    icon?: string;
    component: any;
}

export interface DashboardConfig {
    title: string;
    sidebarItems: SidebarItem[];
  }