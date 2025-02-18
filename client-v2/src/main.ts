import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { AppComponent } from './app/app.component';
import { AppModule } from './app/app.module';
import { NgModule } from '@angular/core';


platformBrowserDynamic().bootstrapModule(AppModule)
  .catch((err) => console.error(err));
