package uk.tees.b1162802.boro.ui.forgotPass;

 class ForgotPasswordUserView {
     private String display;
     //... other data fields that may be accessible to the UI

     ForgotPasswordUserView(String displayName) {
         this.display = displayName;
     }

     String getDisplay() {
         return display;
     }
}
