import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PlayRoutingModule } from './play-routing.module';
import { PlayComponent } from './play.component';
import {FormsModule} from "@angular/forms";


@NgModule({
  declarations: [PlayComponent],
    imports: [
        CommonModule,
        PlayRoutingModule,
        FormsModule
    ]
})
export class PlayModule { }
