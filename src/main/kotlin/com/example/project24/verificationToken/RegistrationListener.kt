package com.example.project24.verificationToken

import com.example.project24.user.User
import com.example.project24.user.UserService
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.MessageSource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import java.util.*

@Component
class RegistrationListener : ApplicationListener<OnRegistrationCompleteEvent> {
    @Autowired
    private val userService: UserService? = null

    @Autowired
    private val messages: MessageSource? = null

    @Autowired
    private val mailSender: JavaMailSender? = null

    override fun onApplicationEvent(event: OnRegistrationCompleteEvent) {
        this.confirmRegistration(event)
    }

    private fun confirmRegistration(event: OnRegistrationCompleteEvent) {
        val user: User = event.getUser()
        val token: String = UUID.randomUUID().toString()
        userService?.createVerificationTokenForUser(user, token)

        val message: MimeMessage? = mailSender?.createMimeMessage()

        val recipientAddress: String = user.email
        val subject = "Registration Confirmation"
        val confirmationUrl
                : String =
            event.getAppUrl() + "/confirm-registration?token=" + token
//        val message =
//            messages!!.getMessage(
//                "message.regSuccessLink",
//                null,
//                event.getLocale()
//            )

        val htmlContent = """
        <html>
        <body>
            <p>To confirm your e-mail address, please click the button below:</p>
            <a href="$confirmationUrl" style="display: inline-block; padding: 10px 20px; font-size: 16px; color: #ffffff; background-color: #007bff; text-decoration: none; border-radius: 5px;">Confirm Email</a>
        </body>
        </html>
    """

        message?.setContent(htmlContent, "text/html")
        message?.subject = subject
        message?.addRecipient(
            MimeMessage.RecipientType.TO,
            InternetAddress(recipientAddress)
        )

        mailSender?.send(message!!)
    }
}
