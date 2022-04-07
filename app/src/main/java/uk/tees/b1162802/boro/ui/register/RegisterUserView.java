package uk.tees.b1162802.boro.ui.register;

class RegisterUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    RegisterUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
