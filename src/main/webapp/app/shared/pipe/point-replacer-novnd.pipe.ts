import { Pipe } from '@angular/core';
import { DecimalPipe } from '@angular/common';

@Pipe({
    name: 'pointReplacerNoVND'
})
export class PointReplacerPipeNoVND extends DecimalPipe {
    transform(value: any, digits?: string): string {
        return value ? super.transform(value, digits).replace(',', '.') + '' : '0';
    }
}
