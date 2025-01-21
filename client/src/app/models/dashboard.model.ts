export interface SidebarItem {
  id: string;
  label?: string;
  icon?: string;
  component: any;
  hidden: boolean;
  inputs?: Record<string, any>; 
}

export interface DashboardConfig {
  title: string;
  sidebarItems: SidebarItem[];
}
