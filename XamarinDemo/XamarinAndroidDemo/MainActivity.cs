using Android.App;
using Android.OS;
using Android.Support.V7.App;
using Android.Runtime;
using Android.Widget;
using Android.Content;
using Android.Net;

namespace XamarinAndroidDemo
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme", MainLauncher = true)]
    public class MainActivity : AppCompatActivity
    {
        IntentFilter intentFilter;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.activity_main);

            var emaiBtn = FindViewById<Button>(Resource.Id.emailBtn);
            var callBtn = FindViewById<Button>(Resource.Id.callBtn);
            var batBtn = FindViewById<Button>(Resource.Id.batBtn);

            emaiBtn.Click += SendEmail;
            callBtn.Click += Call;
            batBtn.Click += CheckBatteryStatus;
        }

        private void Call(object sender, System.EventArgs e)
        {
            Intent intent = new Intent(Intent.ActionDial);
            intent.SetData(Uri.Parse("tel:9988776655"));
            StartActivity(intent);
        }

        private void SendEmail(object sender, System.EventArgs e)
        {
            Intent emailIntent = new Intent(Intent.ActionSend);
            emailIntent.SetType("text/plain");
            emailIntent.PutExtra(Intent.ExtraEmail, "johndoe@example.com");
            emailIntent.PutExtra(Intent.ExtraSubject, "Hello World!");
            emailIntent.PutExtra(Intent.ExtraText, "Hi! I am sending you a test email");

            try
            {
                StartActivity(Intent.CreateChooser(emailIntent, "Send e-mail"));
            }
            catch (ActivityNotFoundException ex)
            {
                Toast.MakeText(this, "There is no email client installed.",
                    ToastLength.Short).Show();
            }
        }

        private void CheckBatteryStatus(object sender, System.EventArgs e)
        {
            intentFilter = new IntentFilter(Intent.ActionBatteryChanged);
            MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver(this);
            RegisterReceiver(broadcastReceiver, intentFilter);
        }
    }

    public class MyBroadcastReceiver : BroadcastReceiver
    {
        private MainActivity mainActivity;

        public MyBroadcastReceiver(MainActivity mainActivity)
        {
            this.mainActivity = mainActivity;
        }

        public override void OnReceive(Context context, Intent intent)
        {
            // status
            int status = intent.GetIntExtra(BatteryManager.ExtraStatus, -1);

            switch (status)
            {
                case 1:         // Unknown
                case 3:         // Discharging
                case 4:         // NotCharging
                case 5:         // Full
                    int level = intent.GetIntExtra(BatteryManager.ExtraLevel, -1);
                    int scale = intent.GetIntExtra(BatteryManager.ExtraScale, -1);
                    float batteryPct = (level / (float)scale) * 100;
                    var batLevel = $"Battery remaining {batteryPct}%";
                    Toast.MakeText(mainActivity, batLevel, ToastLength.Short).Show();
                    break;
                case 2:         // Charging 
                    Toast.MakeText(mainActivity, "Charging", ToastLength.Short).Show();
                    break;
                default:
                    break;

            }
        }
    }
}