from flask import Flask
from flask_mail import Mail, Message
app = Flask(__name__)

# mail = Mail(app)
app.config.update(
    MAIL_DEBUG=True,
	MAIL_SUPPRESS_SEND = False,
	TESTING = False,
	MAIL_SERVER='smtp.qq.com',
	MAIL_PORT=465,
        MAIL_USE_TLS=False,
	MAIL_USE_SSL=True,
	MAIL_USERNAME='1316541911@qq.com',
	MAIL_PASSWORD='frnfrvnevtxjgfed'
)
mail = Mail(app)

def send_email(subject,to,template):
   msg = Message(
        subject,
        sender='1316541911@qq.com',
        recipients=[to],
        html=template
   )
   mail.send(msg)
   print("send email ok")
