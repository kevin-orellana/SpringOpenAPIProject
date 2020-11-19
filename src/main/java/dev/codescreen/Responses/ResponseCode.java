package dev.codescreen.Responses;

/*
* ResponseCode:
      type: string
      description: >-
        The response code sent back to the network for the merchant. Multiple declines
        reasons may exist but only one will be sent back to the network. Advice messages
        will include the response code that was sent on our behalf.
      enum:
        - APPROVED
        - DECLINED
* */
public enum ResponseCode {
    APPROVED,
    DECLINED
}
