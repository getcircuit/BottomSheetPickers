package com.philliphsu.bottomsheetpickers.time.numberpad;


import androidx.annotation.NonNull;

final class NumberPadTimePickerDialogPresenter extends NumberPadTimePickerPresenter
        implements INumberPadTimePicker.DialogPresenter {

    private final DigitwiseTimeParser mTimeParser = new DigitwiseTimeParser(mTimeModel);

    private INumberPadTimePicker.DialogView mView;

    NumberPadTimePickerDialogPresenter(@NonNull INumberPadTimePicker.DialogView view,
                                       @NonNull LocaleModel localeModel,
                                       boolean is24HourMode) {
        super(view, localeModel, is24HourMode);
        mView = view;
    }

    @Override
    public void onStop() {
        super.onStop();
        mView = null;
    }

    @Override
    public void onCancelClick() {
        mView.cancel();
    }

    @Override
    public void onOkButtonClick() {
        try {
            mView.setResult(mTimeParser.getHour(mAmPmState), mTimeParser.getMinute(mAmPmState));
            mView.cancel();
        } catch (IllegalStateException e) {
            // rare crash happening because they manage to press the button without entering valid time
            // just catch it and wait for them to enter
        }
    }

    @Override
    public void onDialogShow() {
        mView.showOkButton();
    }

    @Override
    void updateViewEnabledStates() {
        super.updateViewEnabledStates();
        updateOkButtonState();
    }

    private void updateOkButtonState() {
        mView.setOkButtonEnabled(mTimeParser.checkTimeValid(mAmPmState));
    }
}
