import { Directive, ElementRef, forwardRef, HostListener, Input, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { InputHandler } from './inputHandler';
import { DECIMAL, SUFFIX, THOUSANDS } from 'app/app.constants';

export const CURRENCYMASKDIRECTIVE_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => CurrencyMaskDirective),
    multi: true
};

@Directive({
    selector: '[hgCurrencyMask]',
    providers: [CURRENCYMASKDIRECTIVE_VALUE_ACCESSOR]
})
export class CurrencyMaskDirective implements ControlValueAccessor, OnInit {
    @Input() options: any;

    public inputHandler: InputHandler;

    public optionsTemplate = {
        align: 'right',
        allowNegative: false,
        allowZero: true,
        decimal: DECIMAL,
        precision: 0,
        prefix: '',
        suffix: SUFFIX,
        thousands: THOUSANDS,
        autocomplete: 'off',
        type: 'search'
    };

    constructor(private elementRef: ElementRef) {}

    ngOnInit() {
        this.elementRef.nativeElement.autocomplete = this.optionsTemplate.autocomplete;
        this.elementRef.nativeElement.type = this.optionsTemplate.type;
        this.inputHandler = new InputHandler(this.elementRef.nativeElement, (<any>Object).assign({}, this.optionsTemplate, this.options));
    }

    @HostListener('blur', ['$event'])
    handleBlur(event: any) {
        this.inputHandler.getOnModelTouched().apply(event);
    }

    @HostListener('cut', ['$event'])
    handleCut(event: any) {
        if (!this.isChromeAndroid()) {
            this.inputHandler.handleCut(event);
        }
    }

    @HostListener('input', ['$event'])
    handleInput(event: any) {
        if (this.isChromeAndroid()) {
            this.inputHandler.handleInput(event);
        }
    }

    @HostListener('keydown', ['$event'])
    handleKeydown(event: any) {
        if (!this.isChromeAndroid()) {
            this.inputHandler.handleKeydown(event);
        }
    }

    @HostListener('keypress', ['$event'])
    handleKeypress(event: any) {
        if (!this.isChromeAndroid()) {
            this.inputHandler.handleKeypress(event);
        }
    }

    @HostListener('paste', ['$event'])
    handlePaste(event: any) {
        if (!this.isChromeAndroid()) {
            this.inputHandler.handlePaste(event);
        }
    }

    isChromeAndroid(): boolean {
        return /chrome/i.test(navigator.userAgent) && /android/i.test(navigator.userAgent);
    }

    registerOnChange(callbackFunction: Function): void {
        this.inputHandler.setOnModelChange(callbackFunction);
    }

    registerOnTouched(callbackFunction: Function): void {
        this.inputHandler.setOnModelTouched(callbackFunction);
    }

    setDisabledState(value: boolean): void {
        this.elementRef.nativeElement.disabled = value;
    }

    writeValue(value: number): void {
        this.inputHandler.setValue(value);
    }
}
