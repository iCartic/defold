#ifndef DM_ENGINE_H
#define DM_ENGINE_H

#include <stdint.h>

#include <dlib/message.h>

#include <resource/resource.h>

// Embedded resources
extern char DEBUG_VPC[];
extern uint32_t DEBUG_VPC_SIZE;
extern char DEBUG_FPC[];
extern uint32_t DEBUG_FPC_SIZE;

namespace dmEngine
{
    typedef struct Engine* HEngine;

    HEngine New();
    void Delete(HEngine);

    bool Init(HEngine engine, int argc, char *argv[]);
    int32_t Run(HEngine engine);
    void Exit(HEngine engine, int32_t code);

    void LoadCollection(HEngine engine, const char* collection_name);
    void UnloadCollection(HEngine engine, const char* collection_name);
    void ActivateCollection(HEngine engine, const char* collection_name);

    uint32_t GetFrameCount(HEngine engine);
};

#endif // DM_ENGINE_H
